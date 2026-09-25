package com.example.modid;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class FreeGiveMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                CommandManager.literal("giveitem")
                    .then(CommandManager.argument("item", IdentifierArgumentType.identifier())
                        .suggests((context, builder) ->
                            CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                        .executes(context -> giveItem(context, 1))
                        .then(CommandManager.argument("count", IntegerArgumentType.integer(1, 64))
                            .executes(context -> giveItem(
                                context,
                                IntegerArgumentType.getInteger(context, "count")))
                        )
                    )
            )
        );
    }

    private int giveItem(CommandContext<ServerCommandSource> context, int count) {
        var source = context.getSource();
        var player = source.getPlayer();
        var identifier = IdentifierArgumentType.getIdentifier(context, "item");

        if (!Registries.ITEM.containsId(identifier)) {
            source.sendError(Text.translatable("commands.give.failed", 0));
            return 0;
        }

        Item item = Registries.ITEM.get(identifier);
        ItemStack stack = new ItemStack(item, count);
        boolean inserted = player.getInventory().insertStack(stack);
        if (!stack.isEmpty()) {
            player.dropItem(stack, false);
        }

        source.sendFeedback(
            () -> Text.literal(inserted && stack.isEmpty()
                ? "Given!"
                : "Given, with some items dropped at your feet."),
            false
        );
        return 1;
    }
}