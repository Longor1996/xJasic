package de.longor1996.jasic;

/**
 * This defines the different states the Tokenizer can be in while it's
 * scanning through the source code. Tokenizers are state machines, which
 * means the only data they need to store is where they are in the source
 * code and this one "state" or mode value.
 * 
 * One of the main differences between tokenizing and parsing is this
 * regularity. Because the Tokenizer stores only this one state value, it
 * can't handle nesting (which would require also storing a number to
 * identify how deeply nested you are). The parser is able to handle that.
 */
enum TokenizeState {
    NOT_TOKENIZING, WORD, NUMBER, STRING, COMMENT
}