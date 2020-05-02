package me.cory.enderbow.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.cory.enderbow.EnderbowPlugin;
import me.cory.enderbow.utils.EnderbowUtil;

/**
 * A listener class containing all events for our plugin
 * Currently there is only one event, however we could easily add more 
 * @author Cory Caron
 */
public class EnderbowEvents implements Listener {

	/**
	 * Shoot an ender pearl on bow shoot if the bow is an ender bow
	 * While you might think that our event is low priority,
	 * we've indicated that it has a high priority so it will be run after most other listeners for the same event
	 * This way if another plugin cancels the event we can see that and stop trying to shoot an ender pearl
	 */
	@EventHandler(priority=EventPriority.HIGH)
	public void onEntityShootBow(EntityShootBowEvent event){
		//If another plugin has already cancelled this event, we should stop trying to shoot an ender pearl
		if(event.isCancelled())return;
		
		//Check if the entity that shot the bow is a player
		if(event.getEntity() instanceof Player){
			//Cast the entity to a player now that we confirmed that's the case
			Player player = (Player) event.getEntity();
			
			//Check if the player actually has permission to use the Enderbow and return if not
			if(!player.hasPermission(EnderbowUtil.ENDERBOW_USE_PERM))return;
			
			//Get the item currently in the player's main hand (We know it will be a bow but we'll need to confirm its an Enderbow)
			ItemStack hand = player.getInventory().getItemInMainHand();
			
			//Check if the bow in the player's hand is an Enderbow
			if(EnderbowUtil.isEnderBow(hand)){
				//Check the config for whether ender pearls are required to fire the bow
				if(EnderbowPlugin.getInternalConfig().isUsePearls()){
					//If the player is in creative then we can let them fire the bow regardless of whether they have ender pearls
					if(player.getGameMode() != GameMode.CREATIVE){
						//Check if the player's inventory contains any ender pearls
						if(!player.getInventory().contains(Material.ENDER_PEARL)){
							//Cancel the event so an arrow isn't shot
							event.setCancelled(true);
							
							//Return since the player doesn't have the required ender pearl
							return;
						}
						else{
							//Create an ItemStack of 1 ender pearl
							ItemStack ender = new ItemStack(Material.ENDER_PEARL, 1);
							
							//Use the created ItemStack to remove 1 ender pearl from the player's inventory
							player.getInventory().removeItem(ender);
						}
					}
				}
				//We want to get the arrow entity so we can use it's velocity for the ender pearl then remove it
				Entity arrow = event.getProjectile();
				
				//Since an EnderPearl is a projectile we can create it easily by specifying it's class object and providing the velocity we want to shoot it at
				EnderPearl pearl = player.launchProjectile(EnderPearl.class, arrow.getVelocity());
				
				//Now that we have spawned the ender pearl we want to remove the arrow
				arrow.remove();
				
				//The final step is the set the shooter to the player so it actually teleports them when it hits the ground
				pearl.setShooter(player);
			}
		}
	}
	
}
