package net.simplyrin.simplekaboom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.simplyrin.simplekaboom.Main;

/**
 * Created by SimplyRin on 2018/10/07.
 *
 * Copyright (c) 2017-2018 SimplyRin
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
public class CommandKaboom implements CommandExecutor {

	private Main plugin;

	public CommandKaboom(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("simplekaboom.command.admin")) {
			sender.sendMessage(this.plugin.getPrefix() + "§cYou don't have access to this command!");
			return true;
		}

		if (args.length > 0) {
			if (args[0].contains(",")) {
				for (String target : args[0].split(",")) {
					Player player = this.plugin.getServer().getPlayer(target);
					if (player != null) {
						this.plugin.kaboomPlayer(player, (Player) sender, true);
					}
				}
				return true;
			}
			if (args[0].contains("@a")) {
				for (Player player : this.plugin.getServer().getOnlinePlayers()) {
					this.plugin.kaboomPlayer(player, (Player) sender, true);
					if (!this.plugin.getList().contains(player)) {
						this.plugin.getList().add(player);
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				this.plugin.reloadConfig();
				sender.sendMessage(this.plugin.getPrefix() + "§bConfig has been reloaded!");
				return true;
			}
			Player player = this.plugin.getServer().getPlayer(args[0]);
			if (player != null) {
				this.plugin.kaboomPlayer(player, (Player) sender, true);
				if (!this.plugin.getList().contains(player)) {
					this.plugin.getList().add(player);
				}
				return true;
			}
			sender.sendMessage(this.plugin.getPrefix() + "§cPlayer is not online!");
			return true;
		}
		sender.sendMessage(this.plugin.getPrefix() + "§cUsage: /kaboom <player>");
		return true;
	}

}
