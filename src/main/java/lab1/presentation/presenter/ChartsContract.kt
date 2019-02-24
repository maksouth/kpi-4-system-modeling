package main.presentation.presenter

interface ChartsContract {
    interface Presenter {
        fun start()
        fun exponentialButtonClicked()
        fun normalButtonClicked()
        fun uniformButtonClicked()
    }

    interface View {

        fun showExponentialGenerator()
        fun showNormalGenerator()
        fun showUniformGenerator()

        fun clearCharts()
        fun showBarChart(data: List<Pair<Double, Int>>)
        fun showLineChart(data: List<Pair<Double, Double>>)

        fun showAverage(value: Double)
        fun showDispersion(value: Double)
        fun showVerifiedInfo(value: String)

        fun setSecondParameterDisabled(boolean: Boolean)

        fun setFirstParameterName(value: String)
        fun setSecondParameterName(value: String)

        fun getFirstParameterValue(): String?
        fun getSecondParameterValue(): String?
    }
}