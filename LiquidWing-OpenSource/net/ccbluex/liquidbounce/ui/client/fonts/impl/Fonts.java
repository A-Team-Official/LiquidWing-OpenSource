package net.ccbluex.liquidbounce.ui.client.fonts.impl;


import ad.utils.ClientMain;
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontFamily;
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontManager;
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontRenderer;
import net.ccbluex.liquidbounce.ui.client.fonts.api.FontType;

/**
 * @author Artyom Popov+
 *
 * @since June 30, 2020
 */
@SuppressWarnings("SpellCheckingInspection")
public interface Fonts {

	FontManager FONT_MANAGER = ClientMain.getInstance().getFontManager();
	interface font {
		FontFamily font2 = FONT_MANAGER.fontFamily(FontType.Font);
		final class font35 { public static final FontRenderer font35 = font2.ofSize(18); private font35() {} }
		final class font40 { public static final FontRenderer font40 = font2.ofSize(20); private font40() {} }
	}
	interface NovolineIcon{
		FontFamily NovolineIcon = FONT_MANAGER.fontFamily(FontType.Novoicon);
		final class NovolineIcon75 {public static  final  FontRenderer NovolineIcon75 = NovolineIcon.ofSize(75); private NovolineIcon75() {}}
		final class NovolineIcon45 {public static  final  FontRenderer NovolineIcon45= NovolineIcon.ofSize(35); private NovolineIcon45() {}}
	}

	interface jello {
		FontFamily jello = FONT_MANAGER.fontFamily(FontType.Jello);
		final class jello18 { public static final FontRenderer jello18 = jello.ofSize(18); private jello18() {} }
		final class jello20 { public static final FontRenderer jello20 = jello.ofSize(20); private jello20() {} }
		final class jello45 { public static final FontRenderer jello45 = jello.ofSize(45); private jello45() {} }
	}

	interface tenacity {
		 FontFamily tenacity = FONT_MANAGER.fontFamily( FontType.tenacity);
		final class tenacity18 { public static final  FontRenderer tenacity18 = tenacity.ofSize(18); private tenacity18() {} }
		final class tenacity16 { public static final  FontRenderer tenacity16 = tenacity.ofSize(18); private tenacity16() {} }
		final class tenacity14 { public static final  FontRenderer tenacity14 = tenacity.ofSize(14); private tenacity14() {} }
		final class tenacity20 { public static final  FontRenderer tenacity20 = tenacity.ofSize(20); private tenacity20() {} }
		final class tenacity22 { public static final  FontRenderer tenacity22 = tenacity.ofSize(22); private tenacity22() {} }
	}
	interface tenacitybold {
		 FontFamily tenacitybold = FONT_MANAGER.fontFamily( FontType.tenacitybold);
		final class tenacitybold22 { public static final  FontRenderer tenacitybold22 = tenacitybold.ofSize(22); private tenacitybold22() {} }
		final class tenacitybold13 { public static final  FontRenderer tenacitybold13 = tenacitybold.ofSize(13); private tenacitybold13() {} }
		final class tenacitybold20 { public static final  FontRenderer tenacitybold20 = tenacitybold.ofSize(20); private tenacitybold20() {} }
		final class tenacitybold16 { public static final  FontRenderer tenacitybold16 = tenacitybold.ofSize(16); private tenacitybold16() {} }
		final class tenacitybold18 { public static final  FontRenderer tenacitybold18 = tenacitybold.ofSize(18); private tenacitybold18() {} }
		final class tenacitybold26 { public static final  FontRenderer tenacitybold26 = tenacitybold.ofSize(26); private tenacitybold26() {} }
		final class tenacitybold40 { public static final  FontRenderer tenacitybold40 = tenacitybold.ofSize(40); private tenacitybold40() {} }

	}
	
	interface DebugIcon {
		FontFamily DebugIcon = FONT_MANAGER.fontFamily(FontType.Debug_Icon);
		final class DebugIcon_30 { public static final FontRenderer DebugIcon_30 = DebugIcon.ofSize(30); private DebugIcon_30() {} }
	}

    interface ICONFONT {

		FontFamily ICONFONT = FONT_MANAGER.fontFamily(FontType.ICONFONT);

		final class ICONFONT_16 { public static final FontRenderer ICONFONT_16 = ICONFONT.ofSize(16); private ICONFONT_16() {} }
		final class ICONFONT_20 { public static final FontRenderer ICONFONT_20 = ICONFONT.ofSize(20); private ICONFONT_20() {} }
		final class ICONFONT_24 { public static final FontRenderer ICONFONT_24 = ICONFONT.ofSize(24); private ICONFONT_24() {} }
		final class ICONFONT_32 { public static final FontRenderer ICONFONT_32 = ICONFONT.ofSize(32); private ICONFONT_32() {} }
		final class ICONFONT_35 { public static final FontRenderer ICONFONT_35 = ICONFONT.ofSize(35); private ICONFONT_35() {} }
		final class ICONFONT_50 { public static final FontRenderer ICONFONT_50 = ICONFONT.ofSize(50); private ICONFONT_50() {} }
	}

	interface CheckFont {

		FontFamily CheckFont = FONT_MANAGER.fontFamily(FontType.Check);

		final class CheckFont_16 { public static final FontRenderer CheckFont_16 = CheckFont.ofSize(16); private CheckFont_16() {} }
		final class CheckFont_20 { public static final FontRenderer CheckFont_20 = CheckFont.ofSize(20); private CheckFont_20() {} }
		final class CheckFont_24 { public static final FontRenderer CheckFont_24 = CheckFont.ofSize(24); private CheckFont_24() {} }
		final class CheckFont_32 { public static final FontRenderer CheckFont_32 = CheckFont.ofSize(32); private CheckFont_32() {} }
		final class CheckFont_35 { public static final FontRenderer CheckFont_35 = CheckFont.ofSize(35); private CheckFont_35() {} }
		final class CheckFont_50 { public static final FontRenderer CheckFont_50 = CheckFont.ofSize(50); private CheckFont_50() {} }
	}
	interface SF {

		FontFamily SF = FONT_MANAGER.fontFamily(FontType.SF);
		final class SF_9 { public static final FontRenderer SF_9 = SF.ofSize(9); private SF_9() {} }
		final class SF_11 { public static final FontRenderer SF_11 = SF.ofSize(11); private SF_11() {} }
		final class SF_14 { public static final FontRenderer SF_14 = SF.ofSize(14); private SF_14() {} }
		final class SF_15 { public static final FontRenderer SF_15 = SF.ofSize(15); private SF_15() {} }
		final class SF_16 { public static final FontRenderer SF_16 = SF.ofSize(16); private SF_16() {} }
		final class SF_17 { public static final FontRenderer SF_17 = SF.ofSize(17); private SF_17() {} }
		final class SF_18 { public static final FontRenderer SF_18 = SF.ofSize(18); private SF_18() {} }
		final class SF_19 { public static final FontRenderer SF_19 = SF.ofSize(19); private SF_19() {} }
		final class SF_20 { public static final FontRenderer SF_20 = SF.ofSize(20); private SF_20() {} }
		final class SF_21 { public static final FontRenderer SF_21 = SF.ofSize(21); private SF_21() {} }
		final class SF_22 { public static final FontRenderer SF_22 = SF.ofSize(22); private SF_22() {} }
		final class SF_23 { public static final FontRenderer SF_23 = SF.ofSize(23); private SF_23() {} }
		final class SF_24 { public static final FontRenderer SF_24 = SF.ofSize(24); private SF_24() {} }
		final class SF_25 { public static final FontRenderer SF_25 = SF.ofSize(25); private SF_25() {} }
		final class SF_26 { public static final FontRenderer SF_26 = SF.ofSize(26); private SF_26() {} }
		final class SF_27 { public static final FontRenderer SF_27 = SF.ofSize(27); private SF_27() {} }
		final class SF_28 { public static final FontRenderer SF_28 = SF.ofSize(28); private SF_28() {} }
		final class SF_29 { public static final FontRenderer SF_29 = SF.ofSize(29); private SF_29() {} }
		final class SF_30 { public static final FontRenderer SF_30 = SF.ofSize(30); private SF_30() {} }
		final class SF_31 { public static final FontRenderer SF_31 = SF.ofSize(31); private SF_31() {} }
		final class SF_50 { public static final FontRenderer SF_50 = SF.ofSize(45); private SF_50() {} }
	}


	interface Icons {

		 FontFamily icons = FONT_MANAGER.fontFamily( FontType.icons);
		final class icons_18 { public static final  FontRenderer icons_18 = icons.ofSize(18); private icons_18() {} }
		final class icons_20 { public static final  FontRenderer icons_20 = icons.ofSize(20); private icons_20() {} }
		final class icons_24 { public static final  FontRenderer icons_24 = icons.ofSize(24); private icons_24() {} }
		final class icons_32 { public static final  FontRenderer icons_32 = icons.ofSize(32); private icons_32() {} }
		final class icons_35 { public static final  FontRenderer icons_35 = icons.ofSize(35); private icons_35() {} }
		final class icons_40 { public static final  FontRenderer icons_40 = icons.ofSize(40); private icons_40() {} }
		final class icons_55 { public static final  FontRenderer icons_55 = icons.ofSize(55); private icons_55() {} }

}
	interface tenacityCheck {
		 FontFamily tenacitycheck = FONT_MANAGER.fontFamily( FontType.tenacityCheck);
		final class tenacitycheck35 { public static final  FontRenderer tenacitycheck35 = tenacitycheck.ofSize(35); private tenacitycheck35() {} }
	}
	interface CsgoIcon {

		FontFamily csgoicon = FONT_MANAGER.fontFamily(FontType.csgoicon);
		final class csgoicon_18 { public static final FontRenderer csgoicon_18 = csgoicon.ofSize(18); private csgoicon_18() {} }
		final class csgoicon_20 { public static final FontRenderer csgoicon_20 = csgoicon.ofSize(20); private csgoicon_20() {} }
		final class csgoicon_24 { public static final FontRenderer csgoicon_24 = csgoicon.ofSize(24); private csgoicon_24() {} }
		final class csgoicon_32 { public static final FontRenderer csgoicon_32 = csgoicon.ofSize(32); private csgoicon_32() {} }
		final class csgoicon_35 { public static final FontRenderer csgoicon_35 = csgoicon.ofSize(35); private csgoicon_35() {} }
		final class csgoicon_40 { public static final FontRenderer csgoicon_40 = csgoicon.ofSize(40); private csgoicon_40() {} }
		final class csgoicon_55 { public static final FontRenderer csgoicon_55 = csgoicon.ofSize(55); private csgoicon_55() {} }
		//final class csgoicon_60 { public static final FontRenderer csgoicon_60 = csgoicon.ofSize(60); private csgoicon_60() {} }

	}
	interface SFBOLD {

		FontFamily SFBOLD = FONT_MANAGER.fontFamily(FontType.SFBOLD);

		final class SFBOLD_14 { public static final FontRenderer SFBOLD_14 = SFBOLD.ofSize(14); private SFBOLD_14() {} }
		final class SFBOLD_15 { public static final FontRenderer SFBOLD_15 = SFBOLD.ofSize(15); private SFBOLD_15() {} }
		final class SFBOLD_16 { public static final FontRenderer SFBOLD_16 = SFBOLD.ofSize(16); private SFBOLD_16() {} }
		final class SFBOLD_18 { public static final FontRenderer SFBOLD_18 = SFBOLD.ofSize(18); private SFBOLD_18() {} }
		final class SFBOLD_20 { public static final FontRenderer SFBOLD_20 = SFBOLD.ofSize(20); private SFBOLD_20() {} }
		final class SFBOLD_22 { public static final FontRenderer SFBOLD_22 = SFBOLD.ofSize(22); private SFBOLD_22() {} }
		final class SFBOLD_26 { public static final FontRenderer SFBOLD_26 = SFBOLD.ofSize(26); private SFBOLD_26() {} }
		final class SFBOLD_28 { public static final FontRenderer SFBOLD_28 = SFBOLD.ofSize(28); private SFBOLD_28() {} }
		final class SFBOLD_35 { public static final FontRenderer SFBOLD_35 = SFBOLD.ofSize(35); private SFBOLD_35() {} }
	}



}
