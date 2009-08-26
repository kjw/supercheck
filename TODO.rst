Simple, 10 minute items
=================================================================================

A few hours, but no more than 3
=================================================================================

- A "CmdLineMain" class with main() that takes class file and property name 
  details on the command line, running the tests so defined.
  
- Improve the available verbosity levels in TestRun. SILENT, NORMAL and VERBOSE.
  SILENT is much like now, NORMAL prints some sort of indication that execution
  is on-going, VERBOSE prints full details of each property test.
  
- Gen must support construction of arbitrary primitive arrays.

- Properties should be allowed to take objects that wrap primitives (Boolean,
  etc). TestRun should handle these by creating arbitrary primitive values,
  probably via Gen calls.
  
- Properties should be allowed to take primitive arrays or arrays of arbitrary
  objects.

Involved, more than 3 hours, maybe days
=================================================================================