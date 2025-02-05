/*
 * Copyright (C) 2023 NotEnoughUpdates contributors
 *
 * This file is part of MoulConfig.
 *
 * MoulConfig is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * MoulConfig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MoulConfig. If not, see <https://www.gnu.org/licenses/>.
 *
 */

/**/
package io.github.moulberry.moulconfig.test;

import io.github.moulberry.moulconfig.ChromaColour;
import io.github.moulberry.moulconfig.common.IItemStack;
import io.github.moulberry.moulconfig.forge.ForgeItemStack;
import io.github.moulberry.moulconfig.gui.*;
import io.github.moulberry.moulconfig.gui.component.*;
import io.github.moulberry.moulconfig.internal.RenderUtils;
import io.github.moulberry.moulconfig.observer.ObservableList;
import io.github.moulberry.moulconfig.observer.Property;
import io.github.moulberry.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.moulberry.moulconfig.processor.ConfigProcessorDriver;
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor;
import io.github.moulberry.moulconfig.xml.Bind;
import io.github.moulberry.moulconfig.xml.XMLUniverse;
import lombok.SneakyThrows;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Mod(modid = "moulconfig", name = "MoulConfig")
public class MoulConfigTest {

    GuiScreen screenToOpen = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (screenToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
            screenToOpen = null;
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            TestCategory.TestOverlay o = testConfig.testCategory.testOverlay;
            GlStateManager.pushMatrix();
            o.transform();
            RenderUtils.drawFloatingRect(0, 0, o.getWidth(), o.getHeight());
            int mx = Mouse.getX();
            int my = Minecraft.getMinecraft().displayHeight - Mouse.getY() - 1;
            ChromaColour c = ChromaColour.forLegacyString(testConfig.testCategory.colour);
            RenderUtils.drawGradientRect(
                0, 10, 10, 40, 40, c.getEffectiveColour().getRGB(), c.getEffectiveColour(10).getRGB()
            );
            FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
            fontRendererObj.drawSplitString(
                "Global Mouse X: " + mx + "\n" +
                    "Global Mouse X: " + my + "\n" +
                    "Local Mouse X: " + o.realWorldXToLocalX(mx) + "\n" +
                    "Local Mouse Y: " + o.realWorldYToLocalY(my) + "\n" +
                    "Width: " + o.getWidth() + "\n" +
                    "Height: " + o.getHeight(),
                1, 1, o.getWidth(), 0xFFFFFFFF
            );
            GlStateManager.popMatrix();
        }
    }

    public static TestConfig testConfig = new TestConfig();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (!Boolean.getBoolean("moulconfig.testmod")) return;
        MinecraftForge.EVENT_BUS.register(MoulConfigTest.this);
        MoulConfigProcessor<TestConfig> testConfigMoulConfigProcessor = new MoulConfigProcessor<>(testConfig);
        BuiltinMoulConfigGuis.addProcessors(testConfigMoulConfigProcessor);
        ConfigProcessorDriver.processConfig(testConfig.getClass(), testConfig, testConfigMoulConfigProcessor);
        testConfig.testCategory.text2.whenChanged((oldValue, newValue) ->
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText("Just changed text2 from " + oldValue + " to " + newValue)));
        ClientCommandHandler.instance.registerCommand(new CommandBase() {
            @Override
            public String getCommandName() {
                return "moulconfig";
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return "moulconfig";
            }

            @Override
            public boolean canCommandSenderUseCommand(ICommandSender sender) {
                return true;
            }

            @SneakyThrows
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                sender.addChatMessage(new ChatComponentText("Mouling"));
                if (args.length > 0 && "gui".equals(args[0])) {
                    screenToOpen = new MoulGuiOverlayEditor(testConfigMoulConfigProcessor);
                } else if (args.length > 0 && "testgui".equals(args[0])) {
                    screenToOpen = new GuiScreenElementWrapperNew(new GuiContext(
                        new CenterComponent(new PanelComponent(
                            new ColumnComponent(
                                new TextComponent("Label", 80),
                                new RowComponent(new SwitchComponent(Property.of(false), 100), new TextComponent("Some property"))
                            )
                        ))
                    ));
                } else if (args.length > 0 && "testxml".equals(args[0])) {
                    var xmlUniverse = XMLUniverse.getDefaultUniverse();
                    var gui = xmlUniverse.load(new ObjectBound(), Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("moulconfig:test.xml")).getInputStream());
                    screenToOpen = new GuiScreenElementWrapperNew(new GuiContext(gui));
                } else {
                    screenToOpen = new GuiScreenElementWrapper(new MoulConfigEditor<>(testConfigMoulConfigProcessor));
                }
            }
        });
    }


    public static class Element {
        public Element(String text) {
            this.text = text;
        }

        @Bind
        public String text;
        @Bind
        public boolean enabled;

        @Bind
        public void randomize() {
            text = "§" + "abcdef0123456789".charAt(new Random().nextInt(16)) + text.replaceAll("§.", "");
        }
    }

    public static class ObjectBound {
        @Bind
        public Runnable requestClose;

        @Bind
        public void afterClose() {
            System.out.println("After close");
        }

        @Bind
        public CloseEventListener.CloseAction beforeClose() {
            System.out.println("Before close");
            return CloseEventListener.CloseAction.NO_OBJECTIONS_TO_CLOSE;
        }

        @Bind
        public IItemStack itemStack = ForgeItemStack.of(new ItemStack(Blocks.sand));
        @Bind
        public boolean value;
        @Bind
        public String textField = "";
        @Bind
        public float slider;

        @Bind
        public void addElement() {
            data.add(new Element(textField));
            textField = "";
        }

        @Bind
        public ObservableList<Element> data = new ObservableList<>(new ArrayList<>(Arrays.asList(new Element("Test 1"), new Element("Test 2"), new Element("Test 3"))));
    }
}
