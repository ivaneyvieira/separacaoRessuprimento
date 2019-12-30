package br.com.astrosoft.separacao.view

import com.github.mvysny.karibudsl.v10.label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = SeparacaoLayout::class)
@PageTitle("Duplicar")
class DuplicarView: VerticalLayout() {
  init {
    label("Duplicar")
  }
}