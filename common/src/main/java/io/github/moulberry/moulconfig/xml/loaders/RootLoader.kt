package io.github.moulberry.moulconfig.xml.loaders

import io.github.moulberry.moulconfig.gui.GuiComponent
import io.github.moulberry.moulconfig.xml.ChildCount
import io.github.moulberry.moulconfig.xml.XMLContext
import io.github.moulberry.moulconfig.xml.XMLGuiLoader
import io.github.moulberry.moulconfig.xml.XMLUniverse
import org.w3c.dom.Element
import javax.xml.namespace.QName

class RootLoader : XMLGuiLoader<GuiComponent> {
    override fun createInstance(context: XMLContext<*>, element: Element): GuiComponent {
        return context.getChildFragment(element)
    }

    override fun getName(): QName {
        return XMLUniverse.qName("Root")
    }

    override fun getChildCount(): ChildCount {
        return ChildCount.ONE
    }

    override fun getAttributeNames(): Map<String, Boolean> {
        return mapOf()
    }
}