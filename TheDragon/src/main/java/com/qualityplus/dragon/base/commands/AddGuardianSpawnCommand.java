package com.qualityplus.dragon.base.commands;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.commands.command.AssistantCommand;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.location.LocationUtils;
import com.qualityplus.dragon.api.box.Box;
import eu.okaeri.commons.bukkit.time.MinecraftTimeEquivalent;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.annotation.Delayed;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Component
public final class AddGuardianSpawnCommand extends AssistantCommand {
    private static final String ALTERNATIVE_MESSAGE = "%prefix% &7Guardian spawn was successfully added in &5%thedragon_guardian_spawn_location%&7!";
    private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            Location location = player.getLocation().getBlock().getLocation();

            box.files().guardians().addSpawn(location);

            String message = Optional.ofNullable(box.files().messages().setupMessages.guardianSpawnSet).orElse(ALTERNATIVE_MESSAGE);

            player.sendMessage(StringUtils.color(message
                    .replace("%thedragon_guardian_spawn_location%", LocationUtils.toString(location))
                    .replace("%prefix%", box.files().config().prefix)));
        }else{
            player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax.replace("%usage%", syntax)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box){
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().addGuardianSpawnCommand));
    }
}