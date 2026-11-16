import { readFile } from 'fs';

// Function to check the file format
function checkFileFormat(filePath: string) {
    // Read the first 8 bytes of the file
    readFile(filePath, (err, data) => {
        if (err) {
            console.error("Error reading the file:", err);
            return;
        }

        // Get the first 8 bytes as a string
        const header = data.toString('utf8', 0, 8);

        // Check against known headers
        if (header === 'PBDEMS2\x00') {
            console.log("File is in the new CS2 format.");
        } else if (header === 'HL2DEMO\x00') {
            console.log("File is in the old CS:GO format.");
        } else {
            console.log("Unknown file format.");
        }
    });
}

// Check if a file path is provided as a command-line argument
if (process.argv.length < 3) {
    console.log("Usage: node scriptName.js <path_to_demo_file>");
    process.exit(1);
}

const filePath = process.argv[2];
checkFileFormat(filePath);
