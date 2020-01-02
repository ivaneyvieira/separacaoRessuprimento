package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import com.github.mvysny.karibudsl.v10.label
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = SeparacaoLayout::class)
@PageTitle("Separar")
class SepararView: ViewLayout<SepararViewModel>(), ISepararView {
  init {
    label("Separar")
  }
}