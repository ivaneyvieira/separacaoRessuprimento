package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class SepararViewModel(view: ISepararView): ViewModel<ISepararView>(view) {
}

interface ISepararView: IView {
}