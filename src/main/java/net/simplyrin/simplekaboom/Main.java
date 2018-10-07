package net.simplyrin.simplekaboom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.simplyrin.simplekaboom.commands.CommandKaboom;

/**
 * Created by SimplyRin on 2018/10/07.
 *
 * Copyright (c) 2018 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main extends JavaPlugin implements Listener {

	@Getter
	private List<Player> list = new ArrayList<>();

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		this.getCommand("kaboom").setExecutor(new CommandKaboom(this));
		this.getServer().getPluginManager().registerEvents(this, this);
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

	public void kaboomPlayer(Player player, Player sender, boolean allowLightning) {
		player.setVelocity(new Vector(0.0D, 64.0D, 0.0D));
		player.sendTitle(this.getConfig("Title"), this.getConfig("Sub-Title").replace("%%player%%", sender.getName()));

		if (allowLightning) {
			player.getWorld().strikeLightningEffect(player.getLocation());
		}
	}

	public String getConfig(String path) {
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
	}

	public String getPrefix() {
		return this.getConfig("Prefix");
	}

}
