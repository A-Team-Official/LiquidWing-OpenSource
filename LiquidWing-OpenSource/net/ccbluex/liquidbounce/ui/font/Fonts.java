package net.ccbluex.liquidbounce.ui.font;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts extends MinecraftInstance {

    @FontDetails(fontName = "Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    @FontDetails(fontName = "Roboto Medium", fontSize = 20)
    public static IFontRenderer roboto40;
    @FontDetails(fontName = "flux", fontSize = 18)
    public static IFontRenderer flux;
    @FontDetails(fontName = "flux", fontSize = 23)
    public static IFontRenderer flux45;
    @FontDetails(fontName = "flux", fontSize = 20)
    public static IFontRenderer flux40;
    @FontDetails(fontName = "Roboto Bold", fontSize = 90)
    public static IFontRenderer robotoBold180;
    @FontDetails(fontName = "Product Sans", fontSize = 18)
    public static IFontRenderer productSans35;
    @FontDetails(fontName = "Product Sans", fontSize = 26)
    public static IFontRenderer productSans52;
    @FontDetails(fontName = "Product Sans", fontSize = 15)
    public static IFontRenderer productSans30;
    @FontDetails(fontName = "Product Sans", fontSize = 13)
    public static IFontRenderer productSans26;
    @FontDetails(fontName = "Product Sans", fontSize = 20)
    public static IFontRenderer productSans40;
    @FontDetails(fontName = "Product Sans", fontSize = 23)
    public static IFontRenderer productSans45;
    @FontDetails(fontName = "Product Sans", fontSize = 25)
    public static IFontRenderer productSans50;
    @FontDetails(fontName = "Notification Icon", fontSize = 40)
    public static IFontRenderer notificationIcon80;
    @FontDetails(fontName = "Tenacity", fontSize = 80)
    public static IFontRenderer tenacitybold100;
    @FontDetails(fontName = "Tenacitybold", fontSize = 15)
    public static IFontRenderer tenacitybold30;
    @FontDetails(fontName = "Tenacitybold", fontSize = 13)
    public static IFontRenderer tenacitybold25;
    @FontDetails(fontName = "Tenacitybold", fontSize = 18)
    public static IFontRenderer tenacitybold35;
    @FontDetails(fontName = "Tenacitybold", fontSize = 20)
    public static IFontRenderer tenacitybold40;
    @FontDetails(fontName = "Tenacitybold", fontSize = 21)
    public static IFontRenderer tenacitybold42;
    @FontDetails(fontName = "Neverlose900", fontSize = 18)
    public static IFontRenderer never900_35;
    @FontDetails(fontName = "Neverlose900", fontSize = 20)
    public static IFontRenderer never900_40;
    @FontDetails(fontName = "Neverlose900", fontSize = 23)
    public static IFontRenderer never900_45;

    @FontDetails(fontName = "Neverlose900", fontSize = 55)
    public static IFontRenderer never900_75;
    @FontDetails(fontName = "NewTenacity", fontSize = 15)
    public static IFontRenderer newtenacity30;
    @FontDetails(fontName = "NewTenacity", fontSize = 18)
    public static IFontRenderer newtenacity35;
    @FontDetails(fontName = "NewTenacity", fontSize = 20)
    public static IFontRenderer newtenacity40;
    @FontDetails(fontName = "NewTenacity", fontSize = 23)
    public static IFontRenderer newtenacity45;
    @FontDetails(fontName = "NewTenacity", fontSize = 25)
    public static IFontRenderer newtenacity50;
    @FontDetails(fontName = "NewTenacity", fontSize = 30)
    public static IFontRenderer newtenacity60;
    @FontDetails(fontName = "NewTenacity", fontSize = 13)
    public static IFontRenderer newtenacity26;


    @FontDetails(fontName = "SessionIcon", fontSize = 18)
    public static IFontRenderer sicon35;
    @FontDetails(fontName = "SessionIcon", fontSize = 20)
    public static IFontRenderer sicon40;

    @FontDetails(fontName = "SFUI", fontSize = 20)
    public static IFontRenderer sfui40;
    @FontDetails(fontName = "SFUI", fontSize = 18)
    public static IFontRenderer sfui35;
    @FontDetails(fontName = "SFUI", fontSize = 13)
    public static IFontRenderer sfui25;
    @FontDetails(fontName = "SFUI", fontSize = 15)
    public static IFontRenderer sfui30;
    @FontDetails(fontName = "SFUI", fontSize = 23)
    public static IFontRenderer sfui45;
    @FontDetails(fontName = "Tenacitybold", fontSize = 25)
    public static IFontRenderer tenacitybold50;
    @FontDetails(fontName = "Tenacitybold", fontSize = 50)
    public static IFontRenderer tenacityboldLogo;

    @FontDetails(fontName = "Tenacitybold", fontSize = 14)
    public static IFontRenderer tenacitybold28;
    @FontDetails(fontName = "Tenacitycheck", fontSize = 30)
    public static IFontRenderer tenacitycheck60;
    @FontDetails(fontName = "Posterama", fontSize = 18)
    public static IFontRenderer posterama35;
    @FontDetails(fontName = "Posterama", fontSize = 14)
    public static IFontRenderer posterama28;
    @FontDetails(fontName = "Posterama", fontSize = 20)
    public static IFontRenderer posterama40;
    @FontDetails(fontName = "Roboto Medium", fontSize = 13)
    public static IFontRenderer font25;
    @FontDetails(fontName = "Roboto Medium", fontSize = 15)
    public static IFontRenderer font30;
    @FontDetails(fontName = "Roboto Medium", fontSize = 15)
    public static IFontRenderer font20;
    @FontDetails(fontName = "micon", fontSize = 15)
    public static IFontRenderer micon30;
    @FontDetails(fontName = "micon", fontSize = 8)
    public static IFontRenderer micon15;
    @FontDetails(fontName = "MiSans", fontSize = 15)
    public static IFontRenderer misans30;
    @FontDetails(fontName = "MiSans", fontSize = 18)
    public static IFontRenderer misans35;
    @FontDetails(fontName = "MiSans", fontSize = 20)
    public static IFontRenderer misans40;
    @FontDetails(fontName = "MiSans", fontSize = 22)
    public static IFontRenderer misans50;
    @FontDetails(fontName = "MiSans", fontSize = 30)
    public static IFontRenderer misans60;
    @FontDetails(fontName = "SFBold", fontSize = 15)
    public static IFontRenderer sfbold30;
    @FontDetails(fontName = "SFBold", fontSize = 16)
    public static IFontRenderer sfbold32;
    @FontDetails(fontName = "SFBold", fontSize = 28)
    public static IFontRenderer sfboldGuilogin;
    @FontDetails(fontName = "SFBold", fontSize = 18)
    public static IFontRenderer sfbold35;
    @FontDetails(fontName = "SFBold", fontSize = 20)
    public static IFontRenderer sfbold40;
    @FontDetails(fontName = "SFBold", fontSize = 23)
    public static IFontRenderer sfbold45;
    @FontDetails(fontName = "Roboto Medium", fontSize = 18)
    public static IFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 20)
    public static IFontRenderer font40;
    @FontDetails(fontName = "Noti Icon", fontSize = 30)
    public static IFontRenderer notiicon60;
    @FontDetails(fontName = "Rise", fontSize = 80)
    public static IFontRenderer rise100;
    @FontDetails(fontName = "Rise", fontSize = 18)
    public static IFontRenderer rise35;
    @FontDetails(fontName = "Rise", fontSize = 15)
    public static IFontRenderer rise30;
    @FontDetails(fontName = "Rise", fontSize = 13)
    public static IFontRenderer rise25;
    @FontDetails(fontName = "Rise", fontSize = 20)
    public static IFontRenderer rise40;
    @FontDetails(fontName = "Mon", fontSize = 18)
    public static IFontRenderer mon35;
    @FontDetails(fontName = "Mon", fontSize = 15)
    public static IFontRenderer mon30;
    @FontDetails(fontName = "Mon", fontSize = 20)
    public static IFontRenderer mon40;
    @FontDetails(fontName = "Mon", fontSize = 30)
    public static IFontRenderer mon60;
    @FontDetails(fontName = "pop", fontSize = 15)
    public static IFontRenderer pop30;
    @FontDetails(fontName = "pop", fontSize = 16)
    public static IFontRenderer pop32;
    @FontDetails(fontName = "pop", fontSize = 18)
    public static IFontRenderer pop35;
    @FontDetails(fontName = "pop", fontSize = 13)
    public static IFontRenderer pop25;
    @FontDetails(fontName = "pop", fontSize = 20)
    public static IFontRenderer pop40;
    @FontDetails(fontName = "pop", fontSize = 23)
    public static IFontRenderer pop45;
    @FontDetails(fontName = "pop", fontSize = 13)
    public static IFontRenderer pop26;
    @FontDetails(fontName = "Title", fontSize = 15)
    public static IFontRenderer title30;
    @FontDetails(fontName = "Title", fontSize = 13)
    public static IFontRenderer title25;
    @FontDetails(fontName = "Title", fontSize = 18)
    public static IFontRenderer title35;
    @FontDetails(fontName = "Title", fontSize = 20)
    public static IFontRenderer title40;
    @FontDetails(fontName = "Title", fontSize = 55)
    public static IFontRenderer title75;
    public static void lfont() throws IOException {
        //downloadFonts();
        flux40 = getFont("fluxicon.ttf", 20);
        mon40 = getFont("mon.ttf", 20);
        mon35 = getFont("mon.ttf", 18);
        flux45 = getFont("fluxicon.ttf", 23);
        mon30 = getFont("mon.ttf", 15);
        mon60 = getFont("mon.ttf", 30);
        pop40 = getFont("pop.ttf", 20);

        FontLoaders.F15 = FontLoaders.getFont("misans.ttf", 15, true);
        FontLoaders. F18 = FontLoaders.getFont("misans.ttf", 18, true);
        FontLoaders.F20 = FontLoaders.getFont("misans.ttf", 20, true);
        FontLoaders.F30= FontLoaders.getFont("misans.ttf", 30, true);
        FontLoaders.novologo245 = FontLoaders.getFont("iconnovo.ttf", 45, true);

    }
    public static void loadFonts() throws IOException {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

       // downloadFonts();

        micon15 = getFont("micon.ttf", 18);
        micon30 = getFont("micon.ttf", 25);
        notiicon60 = getFont("nicon.ttf", 30);
        font20 = getFont("sfui.ttf", 10);
        font25 = getFont("sfui.ttf", 13);
        font35 = getFont("sfui.ttf", 18);
        font30 = getFont("sfui.ttf", 15);
        font40 = getFont("sfui.ttf", 20);
        rise100 = getFont("notosans1.ttf", 80);
        rise40 = getFont("notosans1.ttf", 20);
        rise35 = getFont("notosans1.ttf", 18);
        rise30 = getFont("notosans1.ttf", 15);
        rise25 = getFont("notosans1.ttf", 13);

        sfbold35 = getFont("sfbold.ttf", 18);
        sfbold30 = getFont("sfbold.ttf", 15);

        sfbold32 = getFont("sfbold.ttf", 16);
        sfboldGuilogin = getFont("sfbold.ttf", 28);
        sfbold40 = getFont("sfbold.ttf", 20);
        sfbold45 = getFont("sfbold.ttf", 23);
        roboto40 = getFont("roboto-medium.ttf", 20);
        robotoBold180 = getFont("roboto-bold.ttf", 90);
        productSans35 = getFont("product-sans.ttf", 18);
        productSans52 = getFont("product-sans.ttf", 26);
        productSans26 = getFont("product-sans.ttf", 13);
        productSans30 = getFont("product-sans.ttf", 14);
        productSans40 = getFont("product-sans.ttf", 20);
        productSans45 = getFont("product-sans.ttf", 23);
        productSans50 = getFont("product-sans.ttf", 25);
        misans30 = getFont("misans.ttf", 15);
        misans35 = getFont("misans.ttf", 18);
        misans40 = getFont("misans.ttf", 20);
        misans50 = getFont("misans.ttf", 22);
        misans60 = getFont("misans.ttf", 30);
        notificationIcon80 = getFont("nicon.ttf", 40);
        posterama35 = getFont("posterama.ttf", 18);
        posterama40 = getFont("posterama.ttf", 20);
        posterama28 = getFont("posterama.ttf", 14);
        tenacitybold100 = getFont("bold.ttf", 80);
        tenacitybold28 = getFont("bold.ttf", 14);
        tenacitybold25 = getFont("bold.ttf", 13);
        tenacitybold30 = getFont("bold.ttf", 15);
        tenacitybold35 = getFont("bold.ttf", 18);
        tenacitybold40 = getFont("bold.ttf", 20);
        tenacitybold42 = getFont("bold.ttf", 21);

        tenacitybold50= getFont("bold.ttf", 25);
        tenacityboldLogo= getFont("bold.ttf", 50);
        newtenacity26 = getFont("gcf.ttf", 13);
        newtenacity60 = getFont("gcf.ttf", 30);
        newtenacity30 = getFont("gcf.ttf", 15);
        newtenacity35 = getFont("gcf.ttf", 18);
        newtenacity40 = getFont("gcf.ttf", 20);
        newtenacity45 = getFont("gcf.ttf", 23);
        newtenacity50 = getFont("gcf.ttf", 25);
        pop26 = getFont("pop.ttf", 13);
        pop30 = getFont("pop.ttf", 15);
        pop32 = getFont("pop.ttf", 16);
        pop35 = getFont("pop.ttf", 18);
        pop25 = getFont("pop.ttf", 13);
        pop45 = getFont("pop.ttf", 23);
        sfui25 = getFont("sfui.ttf", 13);
        sfui30 = getFont("sfui.ttf", 15);
        sfui35 = getFont("sfui.ttf", 18);
        sfui40 = getFont("sfui.ttf", 20);
        sfui45 = getFont("sfui.ttf", 23);
        sicon35 = getFont("hicon.ttf", 18);
        sicon40 = getFont("hicon.ttf", 20);
        never900_35 = getFont("neverlose900.ttf", 18);
        never900_40 = getFont("neverlose900.ttf", 20);
        never900_45 = getFont("neverlose900.ttf", 23);
        never900_75 = getFont("neverlose900.ttf", 55);
        tenacitycheck60 = getFont("check.ttf", 30);
        flux = getFont("fluxicon.ttf", 18);

        title25 = getFont("title.ttf", 13);
        title30 = getFont("title.ttf", 15);
        title35 = getFont("title.ttf", 18);
        title40 = getFont("title.ttf", 20);
        title75 = getFont("title.ttf", 55);


        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    public static IFontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                Object o = field.get(null);

                if (o instanceof IFontRenderer) {
                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (IFontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e1) {
                ClientUtils.getLogger().fatal("Fontrenderer " + name + " not found.");
            }
        }

        return getFont("default", 35);
    }

    public static FontInfo getFontDetails(final IFontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static List<IFontRenderer> getFonts() {
        final List<IFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return fonts;
    }

    private static IFontRenderer getFont(final String fontName, final int size) {
        Font font;
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            font = awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();
            font = new Font("default", Font.PLAIN, size);
        }

        return classProvider.wrapFontRenderer(new GameFontRenderer(font));
    }
    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    private static void downloadFonts() throws IOException {
        final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "font.zip");

        if (!outputFile.exists()) {
            ClientUtils.getLogger().info("Downloading fonts...");
            HttpUtils.download("http://dl.liquidwing.cc/font.zip", outputFile);
            ClientUtils.getLogger().info("Extract fonts...");
            extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
        }
    }
    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }


        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }
}