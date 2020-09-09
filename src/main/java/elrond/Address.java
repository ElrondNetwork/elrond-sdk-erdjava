package elrond;

import org.bouncycastle.util.encoders.Hex;

import elrond.Exceptions.ErrAddress;

public class Address {
    static final String HRP = "erd";
    static final int PUBKEY_LENGTH = 32;
    static final int PUBKEY_STRING_LENGTH = PUBKEY_LENGTH * 2; // hex-encoded
    static final int BECH32_LENGTH = 62;

    private final String valueHex;

    private Address(String valueHex) {
        this.valueHex = valueHex;
    }

    public static Address fromBech32(String value) throws Exceptions.ErrAddress {
        var bech32Data = Bech32.decode(value);
        if (!bech32Data.hrp.equals(HRP)) {
            throw new Exceptions.ErrAddressBadHrp();
        }

        var decodedBytes = Bech32.convertBits(bech32Data.data, 5, 8, false);
        var hex = new String(Hex.encode(decodedBytes));
        return new Address(hex);
    }

    public static Address fromHex(String value) throws Exceptions.ErrAddress {
        var decoded = Hex.decode(value);
        var encodedAgain = new String(Hex.encode(decoded));
        var isValid = encodedAgain.equals(value);
        if (!isValid) {
            throw new Exceptions.ErrAddressCannotCreate(value);
        }

        return new Address(value);
    }

    public String hex() {
        return this.valueHex;
    }

    public byte[] pubkey() {
        return Hex.decode(this.valueHex);
    }

    public String bech32() throws Exceptions.ErrAddress {
        var pubkey = this.pubkey();
        var address = Bech32.encode(HRP, Bech32.convertBits(pubkey, 8, 5, true));
        return address;
    }

    public static boolean isValidBech32(String value) {
        try {
            Address.fromBech32(value);
            return true;
        } catch(ErrAddress error){
            return false;
        }
    }
}