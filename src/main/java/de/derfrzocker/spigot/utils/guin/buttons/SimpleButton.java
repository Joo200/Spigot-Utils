package de.derfrzocker.spigot.utils.guin.buttons;

import de.derfrzocker.spigot.utils.guin.ClickAction;
import de.derfrzocker.spigot.utils.guin.GuiInfo;
import de.derfrzocker.spigot.utils.guin.settings.Setting;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleButton<S extends Setting<S>> implements Button {

    private final S setting;
    private final BiFunction<S, GuiInfo, ItemStack> itemStackFunction;
    private final List<Consumer<ClickAction>> actions = new LinkedList<>();
    private final List<BiPredicate<S, GuiInfo>> conditions = new LinkedList<>();
    private final List<Function<S, ClickType>> clickTypes = new LinkedList<>();

    public SimpleButton(S setting, BiFunction<S, GuiInfo, ItemStack> itemStackFunction, List<Consumer<ClickAction>> actions, List<BiPredicate<S, GuiInfo>> conditions, List<Function<S, ClickType>> clickTypes) {
        this.setting = setting;
        this.itemStackFunction = itemStackFunction;
        this.actions.addAll(actions);
        this.conditions.addAll(conditions);
        this.clickTypes.addAll(clickTypes);
    }

    @Override
    public ItemStack getItemStack(GuiInfo guiInfo) {
        return itemStackFunction.apply(setting, guiInfo);
    }

    @Override
    public void onClick(ClickAction event) {
        ClickType clickType = event.getClickEvent().getClick();
        if (clickTypes.stream().map(clickFunction -> clickFunction.apply(setting)).anyMatch(clickType::equals)) {
            actions.forEach(consumer -> consumer.accept(event));
        } else {
            event.getClickEvent().setCancelled(true);
        }
    }

    @Override
    public boolean shouldPlace(GuiInfo guiInfo) {
        return conditions.stream().map(tester -> tester.test(setting, guiInfo)).filter(aBoolean -> !aBoolean).findAny().orElse(true);
    }
}
