package com.github.maximkirko.training_2017_android.builders;

/**
 * Created by MadMax on 25.12.2016.
 */

public class A1Data {

    private String E1_text;
    private String E2_text;
    private String S_text;
    private boolean C_value;
    private int I_Id;

    public String getE1_text() {
        return E1_text;
    }

    public String getE2_text() {
        return E2_text;
    }

    public String getS_text() {
        return S_text;
    }

    public boolean isC_value() {
        return C_value;
    }

    public int getI_Id() {
        return I_Id;
    }

    @Override
    public String toString() {
        return "A1Data{" +
                "E1_text='" + E1_text + '\'' +
                ", E2_text='" + E2_text + '\'' +
                ", S_text='" + S_text + '\'' +
                ", C_value=" + C_value +
                ", I_Id=" + I_Id +
                '}';
    }

    public static Builder newBuilder() {
        return new A1Data().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setE1_text(String e1_text) {
            A1Data.this.E1_text = e1_text;

            return this;
        }

        public Builder setE2_text(String e2_text) {
            A1Data.this.E2_text = e2_text;

            return this;
        }

        public Builder setS_text(String s_text) {
            A1Data.this.S_text = s_text;

            return this;
        }

        public Builder setC_value(boolean c_value) {
            A1Data.this.C_value = c_value;

            return this;
        }

        public Builder setI_Id(int imageId) {
            A1Data.this.I_Id = imageId;
            return this;
        }

        public A1Data build() {
            A1Data a1Data = new A1Data();
            a1Data.E1_text = A1Data.this.E1_text;
            a1Data.E2_text = A1Data.this.E2_text;
            a1Data.S_text = A1Data.this.S_text;
            a1Data.C_value = A1Data.this.C_value;
            a1Data.I_Id = A1Data.this.I_Id;
            return a1Data;
        }

    }
}
