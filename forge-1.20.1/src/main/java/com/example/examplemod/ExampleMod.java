package com.example.examplemod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(ExampleMod.MOD_ID)
public class ExampleMod {
    public static final String MOD_ID = "freegive";

    public ExampleMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("giveitem")
                .then(Commands.argument("item", ItemArgument.item(event.getBuildContext()))
                    .executes(context -> giveItem(context, 1))
                    .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
                        .executes(context -> giveItem(
                            context,
                            IntegerArgumentType.getInteger(context, "count")))
                    )
                )
        );
    }

    private int giveItem(CommandContext<CommandSourceStack> context, int count)
        throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = ItemArgument.getItem(context, "item").createItemStack(count, true);

        boolean inserted = player.getInventory().add(stack);
        if (!stack.isEmpty()) {
            player.drop(stack, false);
        }

        context.getSource().sendSuccess(
            () -> Component.literal(inserted && stack.isEmpty()
                ? "Given!"
                : "Given, with some items dropped at your feet."),
            false
        );
        return 1;
    }
}