// MongoDB initialization script
db = db.getSiblingDB('hrms_db');

db.createCollection('staff');
db.createCollection('leave_documents');
db.createCollection('team_documents');

// Indexes (Spring also creates them via auto-index-creation=true,
// but explicit creation gives control over options)
db.staff.createIndex({ email: 1 }, { unique: true, name: 'idx_staff_email_unique' });
db.staff.createIndex({ name: 1 }, { name: 'idx_staff_name' });
db.leave_documents.createIndex({ staffId: 1 }, { unique: true, name: 'idx_leave_staffId_unique' });
db.team_documents.createIndex({ staffId: 1 }, { unique: true, name: 'idx_team_staffId_unique' });

print('✅  hrms_db initialized with collections and indexes');
