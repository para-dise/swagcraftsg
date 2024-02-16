package me.paradise.swagcraftsg.menus;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import java.util.List;

public class MenuItemHolder {
    @Getter @Setter Component name;
    @Getter @Setter List<Component> lore;

    public MenuItemHolder(Component name, List<Component> lore) {
        this.setName(name);
        this.setLore(lore);
    }
}
