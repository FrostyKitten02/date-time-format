import java.io.Serializable;

//storing day millis in 4-bytes (needs 3-bytes and 3-bits from 0-86.400.000)
//storing months and days in 2-bytes (needs 2-byte and 5-bits from 0-12 for months and 0-31 for days)

//TODO implement custom serialization for this class to minimize storage space used
public class DateTime implements Serializable {
    private byte[] dateContent = new byte[5];

    private DateTime() {}

    //alternative: use millis to store millis since first day of month, and then store month and year seperate!?? mybe better

    //BYTE STRUCTURE:
    //first 27 bits are millis in a day (0-86.400.000) - a bit of space left at last byte could make this more precise if we need to
    //next 5 bits are days (0-31)
    //next 4 bits are months (0-12)
    //next 13 bits are years (0-8192) --could use space left for time zones or and for more precise day millis, or even longer years!
    //total of 49 bits used (6 bytes and 1 bit)
    public static DateTime fromByteArray(byte[] rawBytes) {
        if (rawBytes.length != 7) {
            throw new RuntimeException("Invalid byte array length for DateTime");
        }
        //millis in a day
        //not correct, todo
        long dayChunk1 = rawBytes[0] << 19;
        long dayChunk2 = rawBytes[1] << 11;
        long dayChunk3 = rawBytes[2] << 3;
        long dayChunk4 = rawBytes[3] >> 5;

        // or operator not correct I think
        long dayMillis = dayChunk1 | dayChunk2 | dayChunk3 | dayChunk4;




        return null;
    }
}