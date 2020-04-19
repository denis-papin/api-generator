package com.globaljetlux.hubdb;

import java.io.*;

public class SourceWriter {

    private OutputStreamWriter writer;
    int numberOftab = 0;

    public SourceWriter(File f) throws IOException {
        this.writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
    }

    /**
     *
     * @param str
     */
    public void p( String ...str ) throws IOException {

        assert this.writer != null;

        StringBuilder out = new StringBuilder();

        out.append("    ".repeat(Math.max(0, numberOftab)));

        for (int i = 0; i < str.length; i++) {
            out.append(str[i]);
            if (i+1 < str.length  && !str[i+1].equals(";") ) {
                out.append(" ");
            }
        }
        out.append("\r\n");

        writer.write(out.toString());
    }


    public void inctab(int tab) {
        this.numberOftab += tab;
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
