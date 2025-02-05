package io.github.moulberry.moulconfig.gui.component

import io.github.moulberry.moulconfig.common.KeyboardConstants
import io.github.moulberry.moulconfig.gui.GuiComponent
import io.github.moulberry.moulconfig.gui.GuiImmediateContext
import io.github.moulberry.moulconfig.gui.KeyboardEvent
import io.github.moulberry.moulconfig.gui.MouseEvent
import io.github.moulberry.moulconfig.gui.MouseEvent.Click

class ButtonComponent(element: GuiComponent, insets: Int, val onClick: Runnable) : PanelComponent(element, insets) {
    override fun mouseEvent(mouseEvent: MouseEvent, context: GuiImmediateContext) {
        if (context.isHovered && mouseEvent is Click) {
            val (mouseButton, mouseState) = mouseEvent
            if (mouseState && mouseButton == 0) onClick.run()
        }
    }

    override fun keyboardEvent(event: KeyboardEvent, context: GuiImmediateContext) {
        if (isFocused && event is KeyboardEvent.KeyPressed &&
            event.pressed && event.keycode == KeyboardConstants.enter
        ) {
            onClick.run()
        } else super.keyboardEvent(event, context)
    }
}
