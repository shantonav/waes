## WAES assignment Project

### Problem
Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints.
- <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right

The provided data needs to be diff-ed and the results shall be available on a third end
point
- <host>/v1/diff/<ID>

The results shall provide the following info in JSON format
- If equal return that
- If not of equal size just return that
- If of same size provide insight in where the diffs are, actual diffs are not needed.
        So mainly offsets + length in the data

### Solution

- The solution is Spring boot based and considers the following things
- String is the only allowed object to be Base 64 encoded
- Embedded Mongo DB has been considered for document store.
- Document has been structured around JSON
- REST endpoints are strictly JSON content processing
- Input validation is Javax validation driven with custom validator.
- Proper DB isolation has been considered using Java8 concurrent locks.
- Exception handler has been put to place.
- Custom REST error has been created to handle system exceptions and HTTP errors
- Focus has been given more in integration testing.
- Documentation has been created using Spring RESTDocs and JUnit.
- Code itself is the documentation here.
- ASCIIDoctor style code API doc generation has been done.

### How to run build and run the app

- **Required things :** 
Java 8 ,  
Maven  and (optionally an IDE)

- **To build and run use :** _mvn clean package_ 
(you need to be connected to the internet for downloading the dependencies)

- **Once the app is build** and packaged (which also means test were run unless you chose to skip them)
 under _target/generated-docs/api-doc-html_ you would find valuable soure for test scenarios and example requests and responses. 
  