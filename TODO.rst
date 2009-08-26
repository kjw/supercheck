Simple, 10 minute items
=================================================================================

A few hours, but no more than 3
=================================================================================

- A "CmdLineMain" class with main() that takes class file and property name 
  details on the command line, running the tests so defined.
  
- Improve the available verbosity levels in TestRun. SILENT, NORMAL and VERBOSE.
  SILENT is much like now, NORMAL prints some sort of indication that execution
  is on-going, VERBOSE prints full details of each property test.
  
- Gen must support construction of arbitrary arrays, of any type. Something like
  public <A> A[] arbArray(Class<?> type). But also need primitive type arrays.

- Properties should be allowed to take objects that wrap primitives (Boolean,
  etc). TestRun should handle these by creating arbitrary primitive values,
  probably via Gen calls.

Involved, more than 3 hours, maybe days
=================================================================================