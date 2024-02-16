package me.paradise.swagcraftsg.kits;

import lombok.Getter;
import me.paradise.swagcraftsg.SwagCraftSG;
import me.paradise.swagcraftsg.kits.kit_impls.*;
import me.paradise.swagcraftsg.listeners.CombatLogListener;
import me.paradise.swagcraftsg.utils.DisguiseUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KitChooser {

    private static KitChooser instance;
    private final HashMap<UUID, SwagCraftKit> kitMap = new HashMap<>();
    private HashMap<SwagCraftKit, SwagCraftPlayableKit> kitImpls = new HashMap<>();
    @Getter
    DisguiseUtil disguiseUtil;
    @Getter CombatLogListener combatLogListener = new CombatLogListener();
    private KitAbilityCooldown kitAbilityCooldown = new KitAbilityCooldown();


    public KitChooser() {
        this.disguiseUtil = new DisguiseUtil();
    }


    public boolean chooseKit(Player player, SwagCraftKit kit) {
        System.out.println("Player " + player.getUsername() + " chose kit " + kit.toString());
        if(player.hasPermission("swagcraftsg.kit." + kit.toString()) || SwagCraftSG.isDebugMode()) {
            this.kitMap.put(player.getUuid(), kit);
            return true;
        }
        return false;
    }

    public SwagCraftKit getKit(Player player) {
        return this.kitMap.get(player.getUuid());
    }

    private void setupKit(Player player) {
        if(!this.kitMap.containsKey(player.getUuid())) {
            this.kitMap.put(player.getUuid(), SwagCraftKit.DEFAULT);
        }

        SwagCraftKit playerKit = this.kitMap.get(player.getUuid());
        SwagCraftPlayableKit kit = this.kitImpls.get(playerKit);
        Class kitClass = kit.getClass();

        try {
            Method applyEffects = kitClass.getMethod("applyPregameEffects", Player.class);
            applyEffects.invoke(kit, player);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // pass
        }
    }

    public void initKits() {
        System.out.println("Initializing kits");

        // Add all kits
        this.kitImpls.put(SwagCraftKit.ALCHEMIST, new AlchemistImpl());
        this.kitImpls.put(SwagCraftKit.ARCHER, new ArcherImpl());
        this.kitImpls.put(SwagCraftKit.ASSASSIN, new AssassinImpl());
        this.kitImpls.put(SwagCraftKit.BARBARIAN, new BarbarianImpl());
        this.kitImpls.put(SwagCraftKit.BATMAN, new BatmanImpl());
        this.kitImpls.put(SwagCraftKit.BRUTE, new BruteImpl());
        this.kitImpls.put(SwagCraftKit.BUTCHER, new ButcherImpl());
        this.kitImpls.put(SwagCraftKit.CREEPER, new CreeperImpl());
        this.kitImpls.put(SwagCraftKit.DEFAULT, new DefaultImpl());
        this.kitImpls.put(SwagCraftKit.FISHERMAN, new FishermanImpl());
        this.kitImpls.put(SwagCraftKit.FLOWER, new FlowerImpl());
        this.kitImpls.put(SwagCraftKit.GHOST, new GhostImpl());
        this.kitImpls.put(SwagCraftKit.GOD, new GodImpl());
        this.kitImpls.put(SwagCraftKit.HULK, new HulkImpl(this.kitAbilityCooldown));
        this.kitImpls.put(SwagCraftKit.IRONMAN, new IronmanImpl(this.kitAbilityCooldown));

        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            System.out.println("Initializing kit for player " + player.getUsername());
            this.setupKit(player);
        }

    }

    public void applyKits() {
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            System.out.println("Applying kit for player " + player.getUsername());
            SwagCraftPlayableKit kit = this.kitImpls.get(this.getKit(player));
            if(kit != null) {
                kit.applyEffects(player);
                kit.giveInventory(player);
                kit.registerListeners(player);
            } else {
                System.out.println("[WARNING] Kit " + this.getKit(player).toString() + " is not implemented!");
            };
        }

        for(SwagCraftPlayableKit kit : this.kitImpls.values()) {
            kit.registerGlobalListeners();
        }

        combatLogListener.register();
    }

    public boolean hasKit(Player player, SwagCraftKit kit) {
        return this.kitMap.containsKey(player.getUuid()) && this.kitMap.get(player.getUuid()) == kit;
    }

    public static KitChooser getInstance() {
        if (instance == null) {
            instance = new KitChooser();
        }
        return instance;
    }

    public List<Player> getPlayersWithKit(SwagCraftKit kit) {
        // get all players from this.kitMap that have the kit
        List<Player> players = new ArrayList<>();
        for(UUID uuid : this.kitMap.keySet()) {
            if(this.kitMap.get(uuid) == kit) {
                players.add(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid));
            }
        }
        return players;
    }
}
