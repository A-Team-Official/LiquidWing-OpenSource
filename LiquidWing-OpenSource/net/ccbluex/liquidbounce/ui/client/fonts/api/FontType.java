package net.ccbluex.liquidbounce.ui.client.fonts.api;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
@SuppressWarnings("SpellCheckingInspection")
public enum FontType {
	tenacity("gcf.ttf"),
	tenacitybold("bold.ttf"),
	tenacityCheck("check.ttf"),
	//DM("diramight.ttf"),
	Font("misans.ttf"),
	Jello("jellolight.ttf"),
	ICONFONT("stylesicons.ttf"),
//	FluxICONFONT("flux.ttf"),
	Check("check.ttf"),
	icons("icons.ttf"),
	SF("sfui.ttf"),
	Novoicon("iconnovo.ttf"),
	SFBOLD("sfbold.ttf"),
	Debug_Icon("icon.ttf"),
	csgoicon("icomoon.ttf");



	private final String fileName;

	FontType(String fileName) {
		this.fileName = fileName;
	}

	public String fileName() { return fileName; }
}
