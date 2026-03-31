package org.example.project.presentation.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Цветовая палитра GitHub для KMP приложений
 * Основана на официальном дизайне GitHub (Primer Design System)
 */
@Immutable
data class GitHubColors(
    // ========== ОСНОВНЫЕ ЦВЕТА ==========

    /** Основной брендовый цвет GitHub - для ссылок, кнопок, важных элементов */
    val primary: Color,
    /** Акцентный цвет при наведении (для Android с поддержкой стилуса) */
    val primaryHover: Color,

    /** Цвет успешных операций - merge, закрытые issues, успешные проверки */
    val success: Color,

    /** Цвет опасных действий - удаление, выход, ошибки */
    val danger: Color,

    /** Предупреждающий цвет - внимание, деприкейшн */
    val warning: Color,

    // ========== ЦВЕТА ФОНА ==========

    /** Основной фон приложения */
    val backgroundPrimary: Color,
    /** Фон для выделенных секций, карточек */
    val backgroundSecondary: Color,

    // ========== ЦВЕТА ТЕКСТА ==========

    /** Основной текст */
    val textPrimary: Color,
    /** Вторичный текст - метаданные, даты, второстепенная информация */
    val textSecondary: Color,
    /** Третичный текст - еще менее важный */
    val textTertiary: Color,

    /** Текст на цветном фоне (белый для кнопок) */
    val textOnColor: Color,
    /** Текст ссылок */
    val textLink: Color,

    // ========== ЦВЕТА ГРАНИЦ ==========

    /** Основной цвет границ */
    val borderPrimary: Color,

    // ========== ЦВЕТА СОСТОЯНИЙ ==========

    /** Цвет для открытых issues, pull requests */
    val stateOpen: Color,
    /** Цвет для закрытых issues */
    val stateClosed: Color,
    /** Цвет для мерджей */
    val stateMerged: Color,

    // ========== ЦВЕТА ЯЗЫКОВ ПРОГРАММИРОВАНИЯ ==========

    /** Kotlin */
    val languageKotlin: Color,
    /** Java */
    val languageJava: Color,
    /** JavaScript/TypeScript */
    val languageJavaScript: Color,
    /** Python */
    val languagePython: Color,
    /** Swift */
    val languageSwift: Color,
    /** Go */
    val languageGo: Color,
    /** Rust */
    val languageRust: Color,
    /** C/C++ */
    val languageCpp: Color,
    /** Другие языки (дефолтный цвет) */
    val languageDefault: Color,
)

/**
 * Светлая тема GitHub
 */
val LightGitHubColors = GitHubColors(
    // Основные
    primary = Color(0xFF0969DA),
    primaryHover = Color(0xFF0A58CA),
    success = Color(0xFF1A7F37),
    danger = Color(0xFFCF222E),
    warning = Color(0xFF9A6700),

    // Фоны
    backgroundPrimary = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFFF6F8FA),

    // Текст
    textPrimary = Color(0xFF1F2328),
    textSecondary = Color(0xFF656D76),
    textTertiary = Color(0xFF8C959F),
    textOnColor = Color(0xFFFFFFFF),
    textLink = Color(0xFF0969DA),

    // Границы
    borderPrimary = Color(0xFFD0D7DE),

    // Состояния
    stateOpen = Color(0xFF1A7F37),
    stateClosed = Color(0xFF8250DF),
    stateMerged = Color(0xFF8250DF),

    // Языки программирования
    languageKotlin = Color(0xFFA97BFF),
    languageJava = Color(0xFFB07219),
    languageJavaScript = Color(0xFFF1E05A),
    languagePython = Color(0xFF3572A5),
    languageSwift = Color(0xFFF05138),
    languageGo = Color(0xFF00ADD8),
    languageRust = Color(0xFFDEA584),
    languageCpp = Color(0xFFF34B7D),
    languageDefault = Color(0xFF8C959F)
)

/**
 * Темная тема GitHub
 */
val DarkGitHubColors = GitHubColors(
    // Основные
    primary = Color(0xFF2F81F7),
    primaryHover = Color(0xFF58A6FF),
    success = Color(0xFF3FB950),
    danger = Color(0xFFF85149),
    warning = Color(0xFFD29922),

    // Фоны
    backgroundPrimary = Color(0xFF0D1117),
    backgroundSecondary = Color(0xFF161B22),

    // Текст
    textPrimary = Color(0xFFE6EDF3),
    textSecondary = Color(0xFF8B949E),
    textTertiary = Color(0xFF6E7681),
    textOnColor = Color(0xFFFFFFFF),
    textLink = Color(0xFF2F81F7),

    // Границы
    borderPrimary = Color(0xFF30363D),

    // Состояния
    stateOpen = Color(0xFF3FB950),
    stateClosed = Color(0xFFA371F7),
    stateMerged = Color(0xFFA371F7),

    // Языки программирования
    languageKotlin = Color(0xFFA97BFF),
    languageJava = Color(0xFFB07219),
    languageJavaScript = Color(0xFFF1E05A),
    languagePython = Color(0xFF3572A5),
    languageSwift = Color(0xFFF05138),
    languageGo = Color(0xFF00ADD8),
    languageRust = Color(0xFFDEA584),
    languageCpp = Color(0xFFF34B7D),
    languageDefault = Color(0xFF6E7681)
)

// ========== COMPOSITION LOCAL ДЛЯ ТЕМЫ ==========

internal val LocalGitHubColors = staticCompositionLocalOf<GitHubColors> {
    error("GitHubTheme не инициализирован")
}

object GitHubTheme {
    val colors: GitHubColors
        @Composable
        get() = LocalGitHubColors.current
}

@Composable
fun GitHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkGitHubColors else LightGitHubColors

    CompositionLocalProvider(
        LocalGitHubColors provides colors,
        content = content
    )
}