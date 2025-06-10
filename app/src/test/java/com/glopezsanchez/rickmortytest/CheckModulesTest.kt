package com.glopezsanchez.rickmortytest

import com.glopezsanchez.rickmortytest.di.AppModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verifyAll

@OptIn(KoinExperimentalAPI::class)
class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        AppModule.appModule.verifyAll(
            injections = injectedParameters(
                definition<com.glopezsanchez.rickmortytest.ui.MainViewModel>(com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository::class)
            )
        )
    }
}