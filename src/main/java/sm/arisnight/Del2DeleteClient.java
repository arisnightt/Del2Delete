package sm.arisnight;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Set;

public class Del2DeleteClient implements ClientModInitializer {
	private static final Set<String> DELETE_KEYS = Set.of(
			"selectWorld.delete",
			"selectServer.delete"
	);

	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
				ScreenKeyboardEvents.allowKeyPress(screen).register((s, keyEvent) -> {
					if (keyEvent.key() != InputConstants.KEY_DELETE) {
						return true;
					}
					return !tryDelete(s, keyEvent);
				}));
	}

	private static boolean tryDelete(Screen screen, net.minecraft.client.input.KeyEvent keyEvent) {
		if (screen.getFocused() instanceof EditBox) {
			return false;
		}
		for (GuiEventListener child : screen.children()) {
			if (child instanceof Button button
					&& button.visible
					&& button.active
					&& isDeleteButton(button)) {
				button.onPress(keyEvent);
				return true;
			}
		}
		return false;
	}

	private static boolean isDeleteButton(AbstractWidget widget) {
		return widget.getMessage().getContents() instanceof TranslatableContents tc
				&& DELETE_KEYS.contains(tc.getKey());
	}
}