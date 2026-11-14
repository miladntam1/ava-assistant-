package ir.ava.assistant.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ir.ava.assistant.assistant.core.GoogleSttEngine
import ir.ava.assistant.assistant.core.GooglePersianTts
import ir.ava.assistant.assistant.nlu.IntentParser
import ir.ava.assistant.assistant.nlu.PersianIntentParser
import ir.ava.assistant.assistant.plugins.*
import ir.ava.assistant.assistant.plugins.builtincalls.CallPlugin
import ir.ava.assistant.assistant.plugins.builtinsms.SmsPlugin
import ir.ava.assistant.assistant.plugins.builtinapps.OpenAppPlugin
import ir.ava.assistant.assistant.plugins.builtintimer.TimerPlugin
import ir.ava.assistant.assistant.plugins.builtinsearch.SearchPlugin
import ir.ava.assistant.assistant.plugins.builtindatetime.DatetimePlugin
import ir.ava.assistant.assistant.plugins.builtinchats.ChatPlugin
import ir.ava.assistant.assistant.stt.SttEngine
import ir.ava.assistant.assistant.tts.TtsEngine

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds abstract fun stt(e: GoogleSttEngine): SttEngine
    @Binds abstract fun tts(e: GooglePersianTts): TtsEngine
    @Binds abstract fun parser(p: PersianIntentParser): IntentParser
    @Binds @IntoSet abstract fun call(p: CallPlugin): Plugin
    @Binds @IntoSet abstract fun sms(p: SmsPlugin): Plugin
    @Binds @IntoSet abstract fun app(p: OpenAppPlugin): Plugin
    @Binds @IntoSet abstract fun timer(p: TimerPlugin): Plugin
    @Binds @IntoSet abstract fun search(p: SearchPlugin): Plugin
    @Binds @IntoSet abstract fun dt(p: DatetimePlugin): Plugin
    @Binds @IntoSet abstract fun chat(p: ChatPlugin): Plugin
}
