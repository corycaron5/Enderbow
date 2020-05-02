package me.cory.enderbow.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.cory.enderbow.EnderbowPlugin;

/**
 * A utility class with various static methods to provide a clean easy API
 * We are declaring it as final because there are only utility methods and we don't want anyone accidentally extending this class
 * @author Cory Caron
 */
public final class EnderbowUtil {
	
	//NamespacedKey used for the enderbow recipe
	public static final NamespacedKey ENDERBOW_KEY = new NamespacedKey(EnderbowPlugin.getInstance(), "enderbow");
	
	//Permission string required for using the enderbow
	public static final String ENDERBOW_USE_PERM = "enderbow.use";
	
	//Permission string required to use the enderbow command to give self an enderbow
	public static final String ENDERBOW_GIVE_SELF_PERM = "enderbow.give.self";
	
	//Permission string required to use the enderbow command to give others an enderbow
	public static final String ENDERBOW_GIVE_OTHERS_PERM = "enderbow.give.others";
	
	//Permission string required to reload the config
	public static final String ENDERBOW_RELOAD_PERM = "enderbow.reload";
	
	//List of all registered permissions
	private static final ArrayList<Permission> perms = new ArrayList<>();

	/**
     * Prevent anyone from initializing this class as it is solely to be used for static utility
     */
    private EnderbowUtil() {}
	
	/**
	 * Creates an Enderbow
	 * @return An ItemStack representing an Enderbow
	 */
	public static ItemStack createEnderBow(){
		//Create a Bow
		ItemStack bow = new ItemStack(Material.BOW);
		
		//Get the ItemMeta for the created bow
        ItemMeta meta = bow.getItemMeta();
        
        //Set the Display name of the ItemMeta that we got
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Ender Bow");
        
        //Set the Bow with our updated ItemMeta
        bow.setItemMeta(meta);
        
        //Return the ItemStack that we created
        return bow;
	}
	
	/**
	 * Checks if the given ItemStack is an Enderbow
	 * @param stack The ItemStack to check
	 * @return Returns whether this ItemStack is an Enderbow
	 */
	public static boolean isEnderBow(ItemStack stack){
		//Ensure that the ItemStack is not null, is a Bow, and has ItemMeta + Display name and return false if all of these conditions are not met
		if(stack == null || stack.getType() != Material.BOW || !stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName())return false;
		
		//Check if the Display name is the same as the ItemStack we create with createEnderBow()
		else if(stack.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Ender Bow"))return true;
		
		//Return false if the Display name is not the same
		else return false;
	}
	
	/**
	 * Register Enderbow recipe
	 * @return Whether it successfully added the recipe
	 */
	public static boolean registerEnderBowRecipe(){
		//Get the Enderbow ItemStack
		ItemStack bow = EnderbowUtil.createEnderBow();
		
		//Create a ShapedRecipe object with the NamespacedKey and Enderbow
		ShapedRecipe ender = new ShapedRecipe(ENDERBOW_KEY, bow);
		
		//Set the shape of the recipe by using up to 3 strings with up to 3 characters each
		//Each string must be the same length or the recipe will fail to register
		ender.shape("EEE", 
				    "EBE", 
				    "EEE");
		
		//The material representing the E character in the recipe shape
		ender.setIngredient('E', Material.ENDER_PEARL);
		
		//The material representing the B character in the recipe shape
		ender.setIngredient('B', Material.BOW);
		
		//Add the recipe to the server and store whether it was successful
		boolean success = Bukkit.addRecipe(ender);
		
		//If success, log a message saying so
		if(success)EnderbowPlugin.getInstance().getLogger().fine("Registered recipe: " + ENDERBOW_KEY.getNamespace()+":"+ENDERBOW_KEY.getKey());
		
		//If failure, log a message saying so
		else EnderbowPlugin.getInstance().getLogger().fine("Failed to register recipe: " + ENDERBOW_KEY.getNamespace()+":"+ENDERBOW_KEY.getKey());
		
		//Return the result
		return success;
	}
	
	/**
	 * Unregister Enderbow recipe
	 * @return Whether it successfully removed the recipe
	 */
	public static boolean unregisterEnderBowRecipe(){
		//Remove the recipe from the server and store whether it was successful
		boolean success = Bukkit.removeRecipe(ENDERBOW_KEY);
		
		//If success, log a message saying so
		if(success)EnderbowPlugin.getInstance().getLogger().fine("Unregistered recipe: " + ENDERBOW_KEY.getNamespace()+":"+ENDERBOW_KEY.getKey());
		
		//If failure, log a message saying so
		else EnderbowPlugin.getInstance().getLogger().fine("Failed to unregister recipe: " + ENDERBOW_KEY.getNamespace()+":"+ENDERBOW_KEY.getKey());
		
		//Return the result
		return success;
	}
	
	/**
	 * Register all Enderbow permissions
	 */
	public static void registerPermissions(){
		//Create the permissions and store them in a list
		//The list is mainly used internally but a getter could be used to grant other developers access to the list
		//For a permission we only need to have a string representing the key, however it's best to include the description and who has the permission by default
		perms.add(new Permission(ENDERBOW_USE_PERM, "Allows player to use enderbow", PermissionDefault.TRUE));
		perms.add(new Permission(ENDERBOW_GIVE_SELF_PERM, "Allows player give themselves an enderbow", PermissionDefault.OP));
		perms.add(new Permission(ENDERBOW_GIVE_OTHERS_PERM, "Allows player give others an enderbow", PermissionDefault.OP));
		perms.add(new Permission(ENDERBOW_RELOAD_PERM, "Allows players to reload the config", PermissionDefault.OP));
		
		//Loop through the list and add all the permissions we created
		for(Permission perm : perms){
			Bukkit.getPluginManager().addPermission(perm);
			
			//Log a message that we added the permission
			EnderbowPlugin.getInstance().getLogger().fine("Registered Permission: " + perm.getName());
		}
	}

	/**
	 * Unregister all Enderbow permissions
	 */
	public static void unregisterPermissions(){
		//Remove all permissions that we created
		//Mainly used when disabling the plugin to prevent issues if the permissions are changed and the plugin is enabled again (possibly an update?)
		//While using the /reload command is bad practice, many server owners will do so anyway and that can cause issues if we don't clean up properly
		for(Permission perm : perms){
			Bukkit.getPluginManager().removePermission(perm);
			
			//Log a message that we removed the permission
			EnderbowPlugin.getInstance().getLogger().fine("Unregistered Permission: " + perm.getName());
		}
		
		//Clear the list of permissions incase this method was called but the plugin wasn't disabled
		//If we don't do this then calling registerPermissions() would result in trying to register each permission twice
		perms.clear();
	}
	
}
