package me.paradise.swagcraftsg.utils;

import net.minestom.server.entity.metadata.item.ThrownPotionMeta;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PotionMeta;
import net.minestom.server.potion.PotionType;

public class PotionUtil {
    public static ItemStack getRandomPotion() {
        // Poison, Strength, Speed, Speed 2, Slowness, Damage, Healing, Regen, Regen 2, Fire Resistance, Invisibility
        int random = (int) (Math.random() * 11);

        switch (random) {
            case 0:
                ItemMeta potionMeta = new PotionMeta.Builder().potionType(PotionType.POISON).build();
                ItemStack built = ItemStack.builder(Material.SPLASH_POTION).build();
                return built.withMeta(potionMeta);
            case 1:
                ItemMeta potionMeta1 = new PotionMeta.Builder().potionType(PotionType.STRENGTH).build();
                ItemStack built1 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built1.withMeta(potionMeta1);
            case 2:
                ItemMeta potionMeta2 = new PotionMeta.Builder().potionType(PotionType.SWIFTNESS).build();
                ItemStack built2 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built2.withMeta(potionMeta2);
            case 3:
                ItemMeta potionMeta3 = new PotionMeta.Builder().potionType(PotionType.STRONG_SWIFTNESS).build();
                ItemStack built3 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built3.withMeta(potionMeta3);
            case 4:
                ItemMeta potionMeta4 = new PotionMeta.Builder().potionType(PotionType.SLOWNESS).build();
                ItemStack built4 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built4.withMeta(potionMeta4);
            case 5:
                ItemMeta potionMeta5 = new PotionMeta.Builder().potionType(PotionType.HARMING).build();
                ItemStack built5 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built5.withMeta(potionMeta5);
            case 6:
                ItemMeta potionMeta6 = new PotionMeta.Builder().potionType(PotionType.HEALING).build();
                ItemStack built6 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built6.withMeta(potionMeta6);
            case 7:
                ItemMeta potionMeta7 = new PotionMeta.Builder().potionType(PotionType.REGENERATION).build();
                ItemStack built7 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built7.withMeta(potionMeta7);
            case 8:
                ItemMeta potionMeta8 = new PotionMeta.Builder().potionType(PotionType.STRONG_REGENERATION).build();
                ItemStack built8 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built8.withMeta(potionMeta8);
            case 9:
                ItemMeta potionMeta9 = new PotionMeta.Builder().potionType(PotionType.FIRE_RESISTANCE).build();
                ItemStack built9 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built9.withMeta(potionMeta9);
            case 10:
                ItemMeta potionMeta10 = new PotionMeta.Builder().potionType(PotionType.INVISIBILITY).build();
                ItemStack built10 = ItemStack.builder(Material.SPLASH_POTION).build();
                return built10.withMeta(potionMeta10);
            default:
                System.out.println("[WARNING] Invalid potion type generated!");
                return ItemStack.of(Material.AIR);
        }
    }
}
