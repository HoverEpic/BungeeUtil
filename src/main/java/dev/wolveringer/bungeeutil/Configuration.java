package dev.wolveringer.bungeeutil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.wolveringer.bungeeutil.AsyncCatcher.AsyncCatcherMode;
import dev.wolveringer.configuration.file.YamlConfiguration;

public class Configuration {
	private static YamlConfiguration conf;
	@SuppressWarnings("unused")
	private static List<String> versionsFuture;

	public static AsyncCatcherMode getAsyncMode(){
		if(conf.getBoolean("async-catcher.enabled")) {
			return AsyncCatcherMode.valueOf(conf.getString("async-catcher.mode"));
		} else {
			return AsyncCatcherMode.DISABLED;
		}
	}

	public static String getByteBuffType() {
		return conf.getString("byteBuffType");
	}

	public static YamlConfiguration getConfig() {
		return conf;
	}

	public static HandleErrorAction getHandleExceptionAction(){
		return HandleErrorAction.valueOf(conf.getString("network.exception"));
	}

	public static String getLastVersion(){
		return conf.getString("lastVersion");
	}

	public static int getLoadingBufferSize(){
		return conf.getInt("loading.inject.buffer-size");
	}

	@SuppressWarnings("deprecation")
	public static void init(){
		conf = YamlConfiguration.loadConfiguration(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+i18n.getString("configuration.name")));
		conf.setDefaults(YamlConfiguration.loadConfiguration(Configuration.class.getResourceAsStream("/"+i18n.getString("configuration.name"))));
		conf.options().copyHeader(true);
		conf.options().copyDefaults(true);
		try{
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+i18n.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public static boolean isBossBarhandleEnabled(){
		return conf.getBoolean("utils.bossbar");
	}

	public static boolean isDebugEnabled(){
		return conf.getBoolean("debug.messages");
	}

	public static boolean isFastBoot(){
		return conf.getBoolean("loading.fastboot");
	}
	public static boolean isGCEnabled(){
		return conf.getBoolean("system.gc.enabled");
	}
	public static boolean isQuietBoot(){
		return conf.getBoolean("loading.quiet");
	}
	public static boolean isScoreboardhandleEnabled(){
		return conf.getBoolean("utils.scoreboard");
	}
	public static boolean isSyncInventoryClickActive(){
		return conf.getBoolean("inventory.synchandle");
	}
	public static boolean isTerminalColored(){
		return conf.getBoolean("terminal.colored");
	}
	public static boolean isTimingsActive(){
		return conf.getBoolean(i18n.getString("configuration.timings"));
	}
	public static boolean isUpdaterActive(){
		return conf.getBoolean(i18n.getString("configuration.updater"));
	}
	public static boolean ramStatistics() {
		return conf.getBoolean("debug.ram-statistics");
	}
	public static void setLastVersion(String oldVerstion){
		if(oldVerstion == null){
			conf.set("lastVersion", null);
		} else {
			conf.set("lastVersion", oldVerstion);
		}
		try {
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+i18n.getString("configuration.name")));
		} catch (IOException e) {
		}
	}
	public static void setTimingsActive(boolean enabled) {
		conf.set(i18n.getString("configuration.timings"), enabled);
		try{
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+i18n.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
