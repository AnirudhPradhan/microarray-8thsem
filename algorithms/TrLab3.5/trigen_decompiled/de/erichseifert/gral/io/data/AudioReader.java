/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.data.AbstractDataReader;
import de.erichseifert.gral.util.Messages;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioReader
extends AbstractDataReader {
    public AudioReader(String string) {
        super(string);
        this.setDefault("factor", 1.0);
        this.setDefault("offset", 0.0);
    }

    @Override
    public DataSource read(InputStream inputStream, Class<? extends Comparable<?>> ... object) throws IOException {
        try {
            inputStream = AudioSystem.getAudioInputStream(inputStream);
        }
        catch (UnsupportedAudioFileException unsupportedAudioFileException) {
            throw new IOException(unsupportedAudioFileException);
        }
        object = new DataTable(Double.class);
        double d = ((Number)this.getSetting("factor")).doubleValue();
        double d2 = ((Number)this.getSetting("offset")).doubleValue();
        int n = ((AudioInputStream)inputStream).getFormat().getSampleSizeInBits();
        byte[] byArray = new byte[n / 8];
        while (((AudioInputStream)inputStream).read(byArray) >= 0) {
            int n2 = byArray[0];
            if (byArray.length == 1) {
                n2 <<= 8;
            } else if (byArray.length == 2) {
                n2 = n2 & 0xFF | byArray[1] << 8;
            }
            double d3 = d * (double)n2 + d2;
            ((DataTable)object).add(Double.valueOf(d3));
        }
        return object;
    }

    static {
        AudioReader.addCapabilities(new IOCapabilities("WAV", Messages.getString("DataIO.wavDescription"), "audio/wav", new String[]{"wav"}));
    }
}

