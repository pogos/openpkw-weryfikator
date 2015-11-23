package org.openpkw.qr.parser;

/**
 * Enum contains indexes in QR
 * @author Sebastian Pogorzelski
 */
enum QrIndex {

    TERRITORIAL_CODE(0),
    PERIPHERY_NUMBER(1),

    VOTINGCARDS_TOTALENTITLEDTOVOTE(2),
    VOTINGCARDS_TOTALCARDS(3),
    VOTINGCARDS_UNUSEDCARDS(4),
    VOTINGCARDS_REGULARVOTERS(5),
    VOTINGCARDS_REPRESENTATIVEVOTERS(6),
    VOTINGCARDS_CERTIFICATEVOTERS(7),

    CORRESPONDENCEVOTING_ISSUEDPACKAGES(8),
    CORRESPONDENCEVOTING_RECEIVEDREPLYENVELOPES(9),
    CORRESPONDENCEVOTING_MISSINGSTATEMENT(10),
    CORRESPONDENCEVOTING_MISSINGSIGNATUREONSTATEMENT(11),
    CORRESPONDENCEVOTING_MISSINGENVELOPEFORVOTINGCARD(12),
    CORRESPONDENCEVOTING_UNSEALEDENVELOPE(13),
    CORRESPONDENCEVOTING_ENVELOPESTHROWNTOBALLOTBOX(14),

    VOTINGCARDS_CARDSFROMBALLOTBOX(15),
    VOTINGCARDS_CARDSFROMENVELOPES(16),
    VOTINGCARDS_INVALIDCARDS(17),
    VOTINGCARDS_VALIDCARDS(18),
    VOTINGCARDS_INVALIDVOTES(19),
    VOTINGCARDS_VALIDVOTES(20),


    CANDIDATES_START(22);

    private int index;

     QrIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}