package net.nikr.eve.jeveasset;

import java.util.List;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nikr.eve.jeveasset.data.api.accounts.OwnerType;
import net.nikr.eve.jeveasset.data.profile.ProfileData;
import net.nikr.eve.jeveasset.data.profile.ProfileManager;
import net.nikr.eve.jeveasset.gui.shared.Updatable;
import net.nikr.eve.jeveasset.io.online.PriceDataGetter;
import net.nikr.eve.jeveasset.io.online.Updater;

public abstract class AbstractProgram {

    protected AbstractProgram() {
        this.updater = new Updater();
        this.updatable = new Updatable(this);
        this.profileManager = new ProfileManager();

        profileManager.searchProfile();
        //profileManager.loadProfileWithoutCaching();
        profileManager.loadActiveProfile();
        profileData = new ProfileData(profileManager);
        
      //Can not update profile data now - list needs to be empty doing creation...
        priceDataGetter = new PriceDataGetter();
        priceDataGetter.load();
        this.localData = updater.getLocalData();
    }
    
    protected AbstractProgram(final boolean load) {
        profileData = null;
        profileManager = null;
        priceDataGetter = null;
        localData = null;
    }
    
    protected static final Logger LOG = LoggerFactory.getLogger(Program.class);

    protected enum ProgramAction {
        TIMER
    }
    //Major.Minor.Bugfix [Release Candidate n] [BETA n] [DEV BUILD #n];
    public static final String PROGRAM_VERSION = "5.3.1 DEV BUILD 1";
    public static final String PROGRAM_NAME = "jEveAssets";
    public static final String PROGRAM_HOMEPAGE = "https://eve.nikr.net/jeveasset";
    public static final boolean PROGRAM_DEV_BUILD = false;

    protected static boolean debug = false;
    protected static boolean forceUpdate = false;
    protected static boolean forceNoUpdate = false;
    protected static boolean portable = false;
    protected static boolean lazySave = false;
    protected static boolean backgroundMode = false;

    //Misc
    protected Updater updater;
    protected Timer timer;
    protected Updatable updatable;

    //Data
    protected final ProfileData profileData;
    protected final ProfileManager profileManager;
    protected final PriceDataGetter priceDataGetter;
    protected final String localData;
    public ProfileManager getProfileManager() {
        return profileManager;
    }
    public PriceDataGetter getPriceDataGetter() {
        return priceDataGetter;
    }
    
    public List<OwnerType> getOwnerTypes() {
        return profileManager.getOwnerTypes();
    }
    
    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(final boolean debug) {
        AbstractProgram.debug = debug;
    }

    public static boolean isForceNoUpdate() {
        return forceNoUpdate;
    }

    public static void setForceNoUpdate(final boolean forceNoUpdate) {
        Program.forceNoUpdate = forceNoUpdate;
    }

    public static boolean isForceUpdate() {
        return forceUpdate;
    }

    public static void setForceUpdate(final boolean forceUpdate) {
        AbstractProgram.forceUpdate = forceUpdate;
    }

    public static void setPortable(final boolean portable) {
        AbstractProgram.portable = portable;
    }

    public static void setLazySave(final boolean lazySave) {
        AbstractProgram.lazySave = lazySave;
    }
    
    public static void setBackgroundMode(final boolean backgroundMode) {
        AbstractProgram.backgroundMode = backgroundMode;
    }
    
}
