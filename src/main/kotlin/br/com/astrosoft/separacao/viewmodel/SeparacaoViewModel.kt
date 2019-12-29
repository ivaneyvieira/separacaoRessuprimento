package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class SeparacaoViewModel(view: ISeparacaoView): ViewModel<ISeparacaoView>(view) {}

interface ISeparacaoView: IView {}
