package com.todorus.telegrambot;
import com.todorus.telegrambot.control.BotClient;
import com.todorus.telegrambot.control.BotController;
import com.todorus.telegrambot.control.RetroAdapter;
import com.todorus.telegrambot.model.Document;
import com.todorus.telegrambot.model.Message;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link TelegramBotPublisher} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #token})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class TelegramBotPublisher extends Notifier {

    private final String token;
    private final String chatId;

    public static final String ENDPOINT = "https://api.telegram.org";

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public TelegramBotPublisher(String token, String chatId) {
        this.token = token;
        this.chatId = chatId;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getToken() {
        return token;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getChatId() {
        return chatId;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {

        PrintStream logger = listener.getLogger();

        // Prep the message
        int chatId = Integer.parseInt(getChatId());
        Message message = new Message.Builder().setBuild(build).setChatId(chatId).build();
        // Prep the controller
        BotClient botClient = RetroAdapter.getAdapter().create(BotClient.class);
        BotController botController = new BotController(botClient, listener);

        // Send it to the bot
        botController.sendMessage(getToken(), message);

        // If we have a failure, send the buildlog as well
        if(build.getResult().isWorseThan(Result.SUCCESS) && build.getLogFile() != null) {

            Document document = Document.getLogDocument(chatId, build);

            if(document != null) {
                botController.sendDocument(getToken(), document);
            } else {
                logger.println("TelegramBot: Could not find a logfile to send. Kind off ironic, as I'm writing this to it.");
            }
        }

        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    /**
     * Descriptor for {@link TelegramBotPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/TelegramBotPublisher/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean useFrench;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'token'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckToken(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a token");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the token too short?");
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'chatId'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user.
         */
        public FormValidation doCheckChatId(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a chatId");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the chatId too short?");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Post result to a Telegram Bot";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useFrench = formData.getBoolean("useFrench");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }

        /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbox by the naming convention.
         */
        public boolean getUseFrench() {
            return useFrench;
        }
    }
}

