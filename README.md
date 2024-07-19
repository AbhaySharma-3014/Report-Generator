## Current Features Implemented 

• Report Generation App: <br/>
  Developed an application that computes and generates reports based on input files, saving the output files accordingly.
	
• Mapping and Computation: <br/>
  Certain fields in the input file are mapped with a reference file. These mapped values are crucial for subsequent computations.

• Performance: <br/>
  Capable of handling input files exceeding 1GB in size, generating output files in under 30 seconds.
	
• API Endpoint: <br/>
  Provides the functionality to generate reports via an API endpoint ({host}/generateReport).
	
• Job Scheduling: <br/>
  Supports report generation through job scheduling, facilitating automated and periodic report creation.
 
 <br/>

## Assumptions Made

In Input file, since field3 is a String and it is getting multiplied with some value during computation so I assumed that in csv file it will be a numeric value only and not any character string. So I defined field3 as a String in entity model of InputData but I am parsing Double from it during computation.

<br/>

## Future Extensions

• Extensible Interfaces: <br/>
	Implemented interfaces for readers and writers, enabling easy integration of new formats beyond CSV by implementing these interfaces.
	
• Dynamic Entity Model Handling: <br/>
	Designed readers and writers to dynamically handle changes in CSV field structures by reflecting updates only in corresponding entity model classes, without altering existing logic.
	
• Modular Computation Logic: <br/>
	Centralized computation logic within a separate method (getOutputDataPostComputation), facilitating future changes to computation rules without affecting other parts of the application.

• Configurable Application Properties: <br/>
	We can change following details from application.properties file and no changes would be required in code.
  1. input.file.path			*(File path where input file is stored)*
  2. output.file.path			*(File path where output file to be saved)*
  3. reference.file.path  *(File path where reference file is stored)*
  4. chunk.size			      *(It is the number of items to be read and processed before committing a transaction in a chunk-oriented step)*
  5. cron.expression		  *(It is cron expression to be used to schedule the job)*
