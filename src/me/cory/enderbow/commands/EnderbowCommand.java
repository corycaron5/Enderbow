package me.cory.enderbow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.cory.enderbow.EnderbowPlugin;
import me.cory.enderbow.utils.EnderbowUtil;

/**
 * A command executor class that is used whenever the enderbow command is run
 * It's best practice to have each command in its own class implementing CommandExecutor
 * It makes our code much more clean and ensures that our onCommand() will only be executed for the command this executor is registered to
 * @author Cory Caron
 */
public class EnderbowCommand implements CommandExecutor{
	
	/**
	 * Executed when enderbow command is run
	 * CommandSender will generally be a Player, Command Block, or Console but we should always check before doing an action that not all of them support
	 * Command will always be the command that this executor is registered to: In this case enderbow
	 * label is in the case that an alias is used instead of enderbow: We don't really need to worry about this but be aware that it might not be the same as the name of the command
	 * args is an array of all other arguments entered, we always want to check the length of args in case there aren't as many as you would expect!
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		//If the length of arguments is 0 then we can assume that they are trying to give themselves an Enderbow
		if(args.length == 0){
			//Check if the player actually has the permission required
			if(sender.hasPermission(EnderbowUtil.ENDERBOW_GIVE_SELF_PERM)){
				//Before we can give the player a bow we need to make sure that it's actually a player and not a command block or console
				if(sender instanceof Player){
					//Now that we've confirmed it's a player, cast sender to a Player object
					Player player = (Player) sender;
					
					//Create an ItemStack representing an Enderbow
					ItemStack bow = EnderbowUtil.createEnderBow();
					
					//Add the newly created ItemStack to the player's inventory
					player.getInventory().addItem(bow);
					
					//Send the command sender a message confirming that the item was given
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getGiveEnderbowMessage()).replaceAll("%PLAYER%", player.getDisplayName()));
					
					//Return true since the command was successful
					return true;
				}
				else{
					//Send the command sender a message telling them that only players can use this command
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getOnlyPlayersMessage()));
					
					//Return false since the command was not run successfully
					return false;
				}
			}
			else{
				//Send the command sender a message telling them that they don't have permission to use this command
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getNoPermMessage()));
				
				//Return false since the command was not run successfully
				return false;
			}
		}
		//If the length of arguments is 1 then we need to check what the argument is
		else if(args.length == 1){
			//Check if a player exists currently with the specified name
			if(Bukkit.getPlayer(args[0]) != null){
				//Check if the sender has permission to actually run this command
				if(sender.hasPermission(EnderbowUtil.ENDERBOW_GIVE_OTHERS_PERM)){
					//Get the player with the specified name
					Player player = Bukkit.getPlayer(args[0]);
					
					//Create an ItemStack representing an Enderbow
					ItemStack bow = EnderbowUtil.createEnderBow();
					
					//Add the newly created ItemStack to the player's inventory
					player.getInventory().addItem(bow);
					
					//Send the command sender a message confirming that the item was given
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getGiveEnderbowMessage()).replaceAll("%PLAYER%", player.getDisplayName()));
					
					//Return true since the command was successful
					return true;
				}
				else{
					//Send the command sender a message telling them that they don't have permission to use this command
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getNoPermMessage()));
					
					//Return false since the command was not run successfully
					return false;
				}
			}
			//If not a player name then check if they are trying to reload the plugin config
			else if(args[0].equalsIgnoreCase("reload")){
				//Check if the sender has permission to actually run this command
				if(sender.hasPermission(EnderbowUtil.ENDERBOW_RELOAD_PERM)){
					//Reload the internal config
					EnderbowPlugin.getInternalConfig().reloadConfig();
					
					//Send the command sender a message confirming that the item was given
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getConfigReloadedMessage()));
					
					//Return true since the command was successful
					return true;
				}
				else{
					//Send the command sender a message telling them that they don't have permission to use this command
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getNoPermMessage()));
					
					//Return false since the command was not run successfully
					return false;
				}
			}
			else{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EnderbowPlugin.getInternalConfig().getInvalidPlayerMessage()));
				
				//Return false since the command was not run successfully
				return false;
			}
		}
		else{
			//If the player entered more than 1 argument they didn't ender the command correctly, therefore lets send them a message with the correct usage
			sender.sendMessage(ChatColor.AQUA + "/enderbow <player>");
			
			//Return false since the command was not run successfully
			return false;
		}
	}
	
}
