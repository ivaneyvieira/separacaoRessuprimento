package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.viewmodel.SeparacaoViewModel
import br.com.astrosoft.separacao.viewmodel.ISeparacaoView
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Route("")
@Theme(value = Lumo::class, variant = Lumo.DARK)
class SeparacaoLayout: ViewLayout<ISeparacaoView, SeparacaoViewModel>(), ISeparacaoView {

  
  init {
  
  }
  
  override val viewModel: SeparacaoViewModel
    get() = SeparacaoViewModel(this)
}