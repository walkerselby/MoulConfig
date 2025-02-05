package io.github.moulberry.moulconfig.xml.loaders

import io.github.moulberry.moulconfig.gui.component.WhenComponent
import io.github.moulberry.moulconfig.xml.ChildCount
import io.github.moulberry.moulconfig.xml.XMLContext
import io.github.moulberry.moulconfig.xml.XMLGuiLoader
import io.github.moulberry.moulconfig.xml.XMLUniverse
import org.w3c.dom.Element
import javax.xml.namespace.QName

class WhenLoader : XMLGuiLoader<WhenComponent> {
    override fun createInstance(context: XMLContext<*>, element: Element): WhenComponent {
        val fragments = context.getChildFragments(element)
        require(fragments.size == 2)
        return WhenComponent(
            context.getPropertyFromAttribute(element, QName("condition"), Boolean::class.java)!!,
            { fragments[0] },
            { fragments[1] },
        )
    }

    override fun getName(): QName {
        return XMLUniverse.qName("When")
    }

    override fun getChildCount(): ChildCount {
        return ChildCount.TWO
    }

    override fun getAttributeNames(): Map<String, Boolean> {
        return mapOf("condition" to true)
    }
}