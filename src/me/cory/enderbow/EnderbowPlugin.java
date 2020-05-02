package me.cory.enderbow;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.cory.enderbow.commands.EnderbowCommand;
import me.cory.enderbow.configs.Config;
import me.cory.enderbow.events.EnderbowEvents;
import me.cory.enderbow.utils.EnderbowUtil;
/**
 * The main class that extends JavaPlugin
 * This is where we register any event listeners or take any actions that are required on startup or shutdown
 * @author Cory Caron
 */
public class EnderbowPlugin extends JavaPlugin{

	//Logger instance for logging debug messages and information about what the plugin is doing
	private final Logger logger = Logger.getLogger("Enderbow");
	
	//Our internal config object for storing the configuration options that server owners can set
	private static Config config;
	
	//An instance of this plugin for easy access
	private static EnderbowPlugin plugin;
	
	/**
	 * Ran when plugin is enabled
	 * Set static instance of this class
	 * Register event listeners
	 * Create configuration object
	 * Set command executor
	 * Register recipe
	 * Register permissions
	 */
	@Override
	public void onEnable(){
		plugin = this;
		Bukkit.getPluginManager().registerEvents(new EnderbowEvents(), this);
		config = new Config();
		this.getCommand("enderbow").setExecutor(new EnderbowCommand());
		EnderbowUtil.registerEnderBowRecipe();
		EnderbowUtil.registerPermissions();
	}
	
	/**
	 * Ran when plugin is disabled
	 * Remove crafting recipes if the plugin is disabled for any reason
	 * Remove permissions to clean up in case plugin is added again before server restart
	 */
	@Override
	public void onDisable(){
		EnderbowUtil.unregisterEnderBowRecipe();
		EnderbowUtil.unregisterPermissions();
	}
	
	/**
	 * Gets the logger for this plugin
	 */
	public Logger getLogger(){
		return logger;
	}
	
	/**
	 * Gets an instance of this plugin
	 * @return The static instance of this plugin
	 */
	public static EnderbowPlugin getInstance(){
		return plugin;
	}
	
	/**
	 * Gets the internal config
	 * @return The internal config
	 */
	public static Config getInternalConfig(){
		return config;
	}
}
