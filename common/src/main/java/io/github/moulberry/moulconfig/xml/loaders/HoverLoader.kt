package io.github.moulberry.moulconfig.xml.loaders

import io.github.moulberry.moulconfig.gui.component.HoverComponent
import io.github.moulberry.moulconfig.observer.GetSetter
import io.github.moulberry.moulconfig.xml.ChildCount
import io.github.moulberry.moulconfig.xml.XMLContext
import io.github.moulberry.moulconfig.xml.XMLGuiLoader
import io.github.moulberry.moulconfig.xml.XMLUniverse
import org.w3c.dom.Element
import javax.xml.namespace.QName

class HoverLoader : XMLGuiLoader<HoverComponent> {
    override fun createInstance(context: XMLContext<*>, element: Element): HoverComponent {
        val list = context.getPropertyFromAttribute(element, QName("lines"), List::class.java)!!
        return HoverComponent(
            context.getChildFragment(element),
            list as GetSetter<List<String>>
        )
    }

    override fun getName(): QName {
        return XMLUniverse.qName("Hover")
    }

    override fun getChildCount(): ChildCount {
        return ChildCount.ONE
    }

    override fun getAttributeNames(): Map<String, Boolean> {
        return mapOf("lines" to true)
    }
}