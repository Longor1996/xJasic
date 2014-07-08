xJASIC
======

**This is a EXPERIMENTAL library.**
**Usage is only recommended for 'hobby'-projects.**

This is a small library based on the 'Jasic' BASIC-interpreter.
It allows one to execute BASIC-code
(the syntax is slightly different though!).

Please note that this library is very messy
(although there aren't many bugs),
and it should not be used in anything else than 'hobby'-sized projects.

Usage:
    
    Jasic jasic = new Jasic();
    jasic.interpret("PRINT \"Hello, World!\"", null);
