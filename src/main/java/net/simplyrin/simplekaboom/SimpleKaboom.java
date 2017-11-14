package net.simplyrin.simplekaboom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class SimpleKaboom extends JavaPlugin implements Listener {

	private static SimpleKaboom plugin;

	private static List<Player> list = new ArrayList<Player>();

	@Override
	public void onEnable() {
		plugin = this;
		plugin.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();
		String[] args = event.getMessage().split(" ");
		if (args[0].equalsIgnoreCase("/lunch")) {
			event.setCancelled(true);

			kaboomPlayer(sender, false);
			return;
		}

		if (args[0].equalsIgnoreCase("/kaboom") && sender.hasPermission("SimpleLunch.Punch")) {
			event.setCancelled(true);

			if (args.length > 1) {
				if (args[1].contains(",")) {
					String[] players = args[1].split(",");
					for(String p : players) {
						Player player = plugin.getServer().getPlayer(p);
						if(player != null) {
							kaboomPlayer(player, true);
						}
					}
					return;
				}
				if (args[1].contains("@a")) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						kaboomPlayer(player, true);
						if (!list.contains(player)) {
							list.add(player);
						}
					}
					return;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if (player != null) {
					kaboomPlayer(player, true);
					if (!list.contains(player)) {
						list.add(player);
					}
					return;
				}
				sender.sendMessage("§cPlayer is not online!");
				return;
			}
			sender.sendMessage("§cUsage: /kaboom <player>");
			return;
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(list.contains(player)) {
				event.setCancelled(true);
				list.remove(player);
			}
		}
	}

	public static void kaboomPlayer(Player player, boolean allowLightning) {
		player.setVelocity(new Vector(0.0D, 64.0D, 0.0D));

		if(allowLightning) {
			player.getWorld().strikeLightningEffect(player.getLocation());
		}
	}

}
