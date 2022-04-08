package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.CustomEnchantment;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIClickEvent implements Listener {
    @EventHandler
    public void guiClickEvent(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase("enchants")){
            event.setCancelled(true);
            for(CustomEnchantment enchant : CustomEnchantment.values()){
                if(event.getSlot() == enchant.slot){
                    handleEnchant(enchant, (Player) event.getView().getPlayer());
                }
            }
        }
    }

    private void handleEnchant(CustomEnchantment enchant, Player player) {
        ServerPlayer playerData = DataStore.getPlayerData(player);
        if(playerData.tokenBalance <= enchant.price){
            player.sendMessage(ChatColor.RED + "You do not have enough tokens for this enchant!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 5f, 5f);
            return;
        }
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if(itemInMainHand.getItemMeta().hasLore() && itemInMainHand.getItemMeta().getLore().contains(ChatColor.RESET + enchant.displayName + " " + enchant.maxLevel)) return;
        playerData.tokenBalance -= enchant.price;
        ItemMeta meta = itemInMainHand.getItemMeta();
        List<String> lore = new ArrayList<String>();
        boolean success = false;
        if(meta.hasLore()) {
            lore = meta.getLore();
            for (String string : meta.getLore()) {
                if (string.contains(enchant.displayName)) {
                    if (!string.contains("Token") && !enchant.displayName.contains("Token")) continue;
                    int index = lore.indexOf(string);
                    lore.remove(string);
                    String[] parts = string.split(" ");
                    int oldLevel = 0;
                    if (string.contains("Token")) {
                        oldLevel = Integer.parseInt(parts[2]);
                    } else {
                        oldLevel = Integer.parseInt(parts[1]);
                    }
                    parts[1] = String.valueOf(oldLevel + 1);
                    lore.add(index, parts[0] + parts[1] + (parts.length > 2 ? parts[2] : ""));
                    success = true;
                    break;
                }
            }
        }
        if(!success){
            lore.add(ChatColor.RESET + enchant.displayName + " 1");
        }
        meta.setLore(lore);
        itemInMainHand.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Successfully enchanted item for " + enchant.price + " tokens!");
    }
}
