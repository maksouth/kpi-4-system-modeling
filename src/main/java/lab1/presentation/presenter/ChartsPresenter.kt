package main.presentation.presenter

import lab1.verifier.ExpVerifier
import lab1.verifier.NormalVerifier
import lab1.verifier.UniformVerifier
import lab1.verifier.Verifier
import main.presentation.model.RngModel
import main.rng.*

const val GENERATED_NUMBERS_SIZE = 10000
const val INTERVALS = 20

class ChartsPresenter(
    private val view: ChartsContract.View
): ChartsContract.Presenter {

    private val rngModel = RngModel()
    private val defaultGenerator = DefaultGenerator()

    override fun start() {
        exponentialButtonClicked()
    }

    override fun exponentialButtonClicked() {
        val lambdaText = view.getFirstParameterValue() ?: "1.0"

        val lambda = 2.0
        val generator = ExponentialGenerator(lambda, defaultGenerator)

        view.showExponentialGenerator()
        view.setSecondParameterDisabled(true)

        showGeneratorData(generator, ExpVerifier())
    }

    override fun normalButtonClicked() {

        val alphaText = view.getFirstParameterValue() ?: "1.0"
        val sigmaText = view.getSecondParameterValue() ?: "1.0"

        val alpha = alphaText.parse()
        val sigma = sigmaText.parse()
        val generator = NormalGenerator(alpha = alpha, sigma = sigma, generator = defaultGenerator)

        view.showNormalGenerator()
        view.setSecondParameterDisabled(false)

        showGeneratorData(generator, NormalVerifier())
    }

    override fun uniformButtonClicked() {
        val aText = view.getFirstParameterValue() ?: "1.0"
        val cText = view.getSecondParameterValue() ?: "1.0"

        val a = aText.parse()
        val c = cText.parse()
        val generator = UniformGenerator()

        view.showUniformGenerator()
        view.setSecondParameterDisabled(false)

        showGeneratorData(generator, UniformVerifier())
    }

    private fun showGeneratorData(generator: RandomNumberGenerator, verifier: Verifier) {

        val randomSequence = generateSequence { generator.generate() }
            .take(GENERATED_NUMBERS_SIZE)
            .toList()

        val info = verifier.verifyResult(randomSequence)
        view.showVerifiedInfo(info)

        val histograms = rngModel.calculateHistograms(randomSequence, INTERVALS)
        val average = rngModel.calculateAverage(randomSequence)
        val dispersion = rngModel.calculateDispersion(randomSequence)

        view.clearCharts()
        view.showBarChart(histograms)
        view.showAverage(average)
        view.showDispersion(dispersion)
    }



    private fun String.parse() =
            this.toDouble()

}