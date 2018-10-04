package net.simplyrin.simplekaboom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class SimpleKaboom extends JavaPlugin implements Listener {

	private List<Player> list = new ArrayList<Player>();

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();
		String[] args = event.getMessage().split(" ");

		if (args[0].equalsIgnoreCase("/lunch") && sender.hasPermission("simplekaboom.command.lunch")) {
			event.setCancelled(true);

			if (sender.hasPermission("simplekaboom.command.lunch.effect")) {
				this.kaboomPlayer(sender, sender, true);
			} else {
				this.kaboomPlayer(sender, sender, false);
			}
			return;
		}

		if (args[0].equalsIgnoreCase("/kaboom") && sender.hasPermission("simplekaboom.command.admin")) {
			event.setCancelled(true);

			if (args.length > 1) {
				if (args[1].contains(",")) {
					for (String target : args[1].split(",")) {
						Player player = this.getServer().getPlayer(target);
						if (player != null) {
							this.kaboomPlayer(player, sender, true);
						}
					}
					return;
				}
				if (args[1].contains("@a")) {
					for (Player player : this.getServer().getOnlinePlayers()) {
						this.kaboomPlayer(player, sender, true);
						if (!this.list.contains(player)) {
							this.list.add(player);
						}
					}
					return;
				}
				Player player = this.getServer().getPlayer(args[1]);
				if (player != null) {
					this.kaboomPlayer(player, sender, true);
					if (!this.list.contains(player)) {
						this.list.add(player);
					}
					return;
				}
				sender.sendMessage(this.getPrefix() + "§cPlayer is not online!");
				return;
			}
			sender.sendMessage(this.getPrefix() + "§cUsage: /kaboom <player>");
			return;
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (this.list.contains(player)) {
				event.setCancelled(true);
				this.list.remove(player);
			}
		}
	}

	private void kaboomPlayer(Player player, Player sender, boolean allowLightning) {
		player.setVelocity(new Vector(0.0D, 64.0D, 0.0D));
		player.sendTitle("§c§lKABOOM!", "§e§lBy " + sender.getName());

		if (allowLightning) {
			player.getWorld().strikeLightningEffect(player.getLocation());
		}
	}

	private String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', "&7[&cSimpleKaboom&7] &r");
	}

}
