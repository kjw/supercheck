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
  
- If continuing a property after a fail, do so only if the property is not failing
  for _every_ try. Also, if a certain number of tries fail, stop. By default,
  continue if only a few fails occur.
  
- Do not perform a try with the same data, on the same property, more than once 
  in a single test run. If an arbitrary() stops coming back with unseen data
  tell the user.

Involved, more than 3 hours, maybe days
=================================================================================