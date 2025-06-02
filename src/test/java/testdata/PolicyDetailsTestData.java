package testdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enhanced test data class for Policy Details related test scenarios
 * Provides comprehensive test data for policy details, claims, documents, and related functionality
 */
public class PolicyDetailsTestData {
    
    // Enhanced Policy Information
    public static class DetailedPolicyInfo {
        public String policyId;
        public String policyNumber;
        public String status;
        public String type;
        public String category;
        public String premiumAmount;
        public String coverageAmount;
        public String deductible;
        public String effectiveDate;
        public String expirationDate;
        public String insuredName;
        public String agentName;
        public String agentPhone;
        public String agentEmail;
        public List<String> documents;
        public List<ClaimInfo> claims;
        public List<String> beneficiaries;
        public boolean hasExpiryWarning;
        
        public DetailedPolicyInfo() {
            this.documents = new ArrayList<>();
            this.claims = new ArrayList<>();
            this.beneficiaries = new ArrayList<>();
        }
    }
    
    // Claim Information
    public static class ClaimInfo {
        public String claimId;
        public String claimNumber;
        public String claimType;
        public String status;
        public String amount;
        public String dateSubmitted;
        public String dateProcessed;
        public String description;
        
        public ClaimInfo(String claimId, String claimNumber, String claimType, String status, 
                        String amount, String dateSubmitted, String dateProcessed, String description) {
            this.claimId = claimId;
            this.claimNumber = claimNumber;
            this.claimType = claimType;
            this.status = status;
            this.amount = amount;
            this.dateSubmitted = dateSubmitted;
            this.dateProcessed = dateProcessed;
            this.description = description;
        }
    }
    
    // Document Information
    public static class DocumentInfo {
        public String documentName;
        public String documentType;
        public String uploadDate;
        public String fileSize;
        public boolean isDownloadable;
        
        public DocumentInfo(String documentName, String documentType, String uploadDate, 
                           String fileSize, boolean isDownloadable) {
            this.documentName = documentName;
            this.documentType = documentType;
            this.uploadDate = uploadDate;
            this.fileSize = fileSize;
            this.isDownloadable = isDownloadable;
        }
    }
    
    // Sample detailed policies
    public static final DetailedPolicyInfo ACTIVE_AUTO_POLICY_DETAILED = createActiveAutoPolicy();
    public static final DetailedPolicyInfo EXPIRED_HOME_POLICY_DETAILED = createExpiredHomePolicy();
    public static final DetailedPolicyInfo PENDING_LIFE_POLICY_DETAILED = createPendingLifePolicy();
    public static final DetailedPolicyInfo ACTIVE_HEALTH_POLICY_DETAILED = createActiveHealthPolicy();
    
    // Claims test data
    public static final Map<String, List<ClaimInfo>> POLICY_CLAIMS = createPolicyClaims();
    
    // Document test data
    public static final Map<String, List<DocumentInfo>> POLICY_DOCUMENTS = createPolicyDocuments();
    
    // Agent test data
    public static final Map<String, AgentInfo> AGENT_INFORMATION = createAgentInformation();
    
    // Agent Information
    public static class AgentInfo {
        public String name;
        public String phone;
        public String email;
        public String office;
        public String licenseNumber;
        
        public AgentInfo(String name, String phone, String email, String office, String licenseNumber) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.office = office;
            this.licenseNumber = licenseNumber;
        }
    }
    
    // Factory methods for creating test data
    private static DetailedPolicyInfo createActiveAutoPolicy() {
        DetailedPolicyInfo policy = new DetailedPolicyInfo();
        policy.policyId = "POL12345";
        policy.policyNumber = "AUTO-2024-001";
        policy.status = "Active";
        policy.type = "Auto Insurance";
        policy.category = "Personal Vehicle";
        policy.premiumAmount = "$1,200.00";
        policy.coverageAmount = "$50,000.00";
        policy.deductible = "$500.00";
        policy.effectiveDate = "01/15/2024";
        policy.expirationDate = "01/15/2025";
        policy.insuredName = "John Doe";
        policy.agentName = "Sarah Johnson";
        policy.agentPhone = "(555) 123-4567";
        policy.agentEmail = "sarah.johnson@insurance.com";
        policy.hasExpiryWarning = false;
        
        // Add documents
        policy.documents.add("Policy Certificate - AUTO-2024-001.pdf");
        policy.documents.add("Coverage Summary - AUTO-2024-001.pdf");
        policy.documents.add("Payment Schedule - AUTO-2024-001.pdf");
        
        // Add claims
        policy.claims.add(new ClaimInfo("CLM001", "AUTO-CLM-2024-001", "Collision", "Closed", 
                                       "$2,500.00", "03/15/2024", "03/30/2024", "Minor fender bender"));
        policy.claims.add(new ClaimInfo("CLM002", "AUTO-CLM-2024-002", "Comprehensive", "Open", 
                                       "$800.00", "05/20/2024", "", "Windshield replacement"));
        
        return policy;
    }
    
    private static DetailedPolicyInfo createExpiredHomePolicy() {
        DetailedPolicyInfo policy = new DetailedPolicyInfo();
        policy.policyId = "POL67890";
        policy.policyNumber = "HOME-2023-045";
        policy.status = "Expired";
        policy.type = "Home Insurance";
        policy.category = "Homeowners";
        policy.premiumAmount = "$800.00";
        policy.coverageAmount = "$250,000.00";
        policy.deductible = "$1,000.00";
        policy.effectiveDate = "03/01/2023";
        policy.expirationDate = "03/01/2024";
        policy.insuredName = "Jane Smith";
        policy.agentName = "Michael Brown";
        policy.agentPhone = "(555) 987-6543";
        policy.agentEmail = "michael.brown@insurance.com";
        policy.hasExpiryWarning = true;
        
        // Add documents
        policy.documents.add("Policy Certificate - HOME-2023-045.pdf");
        policy.documents.add("Home Inspection Report.pdf");
        policy.documents.add("Renewal Notice - HOME-2023-045.pdf");
        
        // Add beneficiaries
        policy.beneficiaries.add("Robert Smith (Spouse)");
        policy.beneficiaries.add("Emily Smith (Daughter)");
        
        return policy;
    }
    
    private static DetailedPolicyInfo createPendingLifePolicy() {
        DetailedPolicyInfo policy = new DetailedPolicyInfo();
        policy.policyId = "POL11111";
        policy.policyNumber = "LIFE-2024-012";
        policy.status = "Pending";
        policy.type = "Life Insurance";
        policy.category = "Term Life";
        policy.premiumAmount = "$2,400.00";
        policy.coverageAmount = "$500,000.00";
        policy.deductible = "$0.00";
        policy.effectiveDate = "06/01/2024";
        policy.expirationDate = "06/01/2025";
        policy.insuredName = "Michael Johnson";
        policy.agentName = "Lisa Davis";
        policy.agentPhone = "(555) 456-7890";
        policy.agentEmail = "lisa.davis@insurance.com";
        policy.hasExpiryWarning = false;
        
        // Add documents
        policy.documents.add("Application Form - LIFE-2024-012.pdf");
        policy.documents.add("Medical Examination Results.pdf");
        policy.documents.add("Beneficiary Forms - LIFE-2024-012.pdf");
        
        // Add beneficiaries
        policy.beneficiaries.add("Jennifer Johnson (Spouse) - 70%");
        policy.beneficiaries.add("David Johnson (Son) - 15%");
        policy.beneficiaries.add("Sarah Johnson (Daughter) - 15%");
        
        return policy;
    }
    
    private static DetailedPolicyInfo createActiveHealthPolicy() {
        DetailedPolicyInfo policy = new DetailedPolicyInfo();
        policy.policyId = "POL99999";
        policy.policyNumber = "HEALTH-2024-089";
        policy.status = "Active";
        policy.type = "Health Insurance";
        policy.category = "Family Plan";
        policy.premiumAmount = "$3,600.00";
        policy.coverageAmount = "$100,000.00";
        policy.deductible = "$2,000.00";
        policy.effectiveDate = "02/15/2024";
        policy.expirationDate = "02/15/2025";
        policy.insuredName = "Sarah Wilson";
        policy.agentName = "Robert Garcia";
        policy.agentPhone = "(555) 321-0987";
        policy.agentEmail = "robert.garcia@insurance.com";
        policy.hasExpiryWarning = false;
        
        // Add documents
        policy.documents.add("Policy Certificate - HEALTH-2024-089.pdf");
        policy.documents.add("Coverage Details - HEALTH-2024-089.pdf");
        policy.documents.add("Provider Network Directory.pdf");
        policy.documents.add("Prescription Drug Formulary.pdf");
        
        // Add claims
        policy.claims.add(new ClaimInfo("CLM003", "HEALTH-CLM-2024-001", "Medical", "Processed", 
                                       "$450.00", "04/10/2024", "04/15/2024", "Annual checkup"));
        policy.claims.add(new ClaimInfo("CLM004", "HEALTH-CLM-2024-002", "Prescription", "Processed", 
                                       "$75.00", "05/01/2024", "05/03/2024", "Monthly prescription"));
        policy.claims.add(new ClaimInfo("CLM005", "HEALTH-CLM-2024-003", "Specialist", "Open", 
                                       "$250.00", "05/25/2024", "", "Dermatologist consultation"));
        
        return policy;
    }
    
    private static Map<String, List<ClaimInfo>> createPolicyClaims() {
        Map<String, List<ClaimInfo>> claims = new HashMap<>();
        
        // POL12345 claims
        List<ClaimInfo> pol12345Claims = new ArrayList<>();
        pol12345Claims.add(new ClaimInfo("CLM001", "AUTO-CLM-2024-001", "Collision", "Closed", 
                                        "$2,500.00", "03/15/2024", "03/30/2024", "Minor fender bender"));
        pol12345Claims.add(new ClaimInfo("CLM002", "AUTO-CLM-2024-002", "Comprehensive", "Open", 
                                        "$800.00", "05/20/2024", "", "Windshield replacement"));
        claims.put("POL12345", pol12345Claims);
        
        // POL99999 claims
        List<ClaimInfo> pol99999Claims = new ArrayList<>();
        pol99999Claims.add(new ClaimInfo("CLM003", "HEALTH-CLM-2024-001", "Medical", "Processed", 
                                        "$450.00", "04/10/2024", "04/15/2024", "Annual checkup"));
        pol99999Claims.add(new ClaimInfo("CLM004", "HEALTH-CLM-2024-002", "Prescription", "Processed", 
                                        "$75.00", "05/01/2024", "05/03/2024", "Monthly prescription"));
        pol99999Claims.add(new ClaimInfo("CLM005", "HEALTH-CLM-2024-003", "Specialist", "Open", 
                                        "$250.00", "05/25/2024", "", "Dermatologist consultation"));
        claims.put("POL99999", pol99999Claims);
        
        return claims;
    }
    
    private static Map<String, List<DocumentInfo>> createPolicyDocuments() {
        Map<String, List<DocumentInfo>> documents = new HashMap<>();
        
        // POL12345 documents
        List<DocumentInfo> pol12345Docs = new ArrayList<>();
        pol12345Docs.add(new DocumentInfo("Policy Certificate - AUTO-2024-001.pdf", "PDF", 
                                          "01/15/2024", "245 KB", true));
        pol12345Docs.add(new DocumentInfo("Coverage Summary - AUTO-2024-001.pdf", "PDF", 
                                          "01/15/2024", "180 KB", true));
        pol12345Docs.add(new DocumentInfo("Payment Schedule - AUTO-2024-001.pdf", "PDF", 
                                          "01/15/2024", "95 KB", true));
        documents.put("POL12345", pol12345Docs);
        
        // POL67890 documents
        List<DocumentInfo> pol67890Docs = new ArrayList<>();
        pol67890Docs.add(new DocumentInfo("Policy Certificate - HOME-2023-045.pdf", "PDF", 
                                          "03/01/2023", "320 KB", true));
        pol67890Docs.add(new DocumentInfo("Home Inspection Report.pdf", "PDF", 
                                          "02/28/2023", "1.2 MB", true));
        pol67890Docs.add(new DocumentInfo("Renewal Notice - HOME-2023-045.pdf", "PDF", 
                                          "02/01/2024", "150 KB", true));
        documents.put("POL67890", pol67890Docs);
        
        return documents;
    }
    
    private static Map<String, AgentInfo> createAgentInformation() {
        Map<String, AgentInfo> agents = new HashMap<>();
        
        agents.put("POL12345", new AgentInfo("Sarah Johnson", "(555) 123-4567", 
                   "sarah.johnson@insurance.com", "Downtown Office", "AG-12345"));
        agents.put("POL67890", new AgentInfo("Michael Brown", "(555) 987-6543", 
                   "michael.brown@insurance.com", "Suburban Office", "AG-67890"));
        agents.put("POL11111", new AgentInfo("Lisa Davis", "(555) 456-7890", 
                   "lisa.davis@insurance.com", "North Office", "AG-11111"));
        agents.put("POL99999", new AgentInfo("Robert Garcia", "(555) 321-0987", 
                   "robert.garcia@insurance.com", "West Office", "AG-99999"));
        
        return agents;
    }
    
    /**
     * Get detailed policy information by ID
     */
    public static DetailedPolicyInfo getDetailedPolicyById(String policyId) {
        switch (policyId) {
            case "POL12345":
                return ACTIVE_AUTO_POLICY_DETAILED;
            case "POL67890":
                return EXPIRED_HOME_POLICY_DETAILED;
            case "POL11111":
                return PENDING_LIFE_POLICY_DETAILED;
            case "POL99999":
                return ACTIVE_HEALTH_POLICY_DETAILED;
            default:
                return null;
        }
    }
    
    /**
     * Get claims for a specific policy
     */
    public static List<ClaimInfo> getClaimsForPolicy(String policyId) {
        return POLICY_CLAIMS.getOrDefault(policyId, new ArrayList<>());
    }
    
    /**
     * Get documents for a specific policy
     */
    public static List<DocumentInfo> getDocumentsForPolicy(String policyId) {
        return POLICY_DOCUMENTS.getOrDefault(policyId, new ArrayList<>());
    }
    
    /**
     * Get agent information for a specific policy
     */
    public static AgentInfo getAgentForPolicy(String policyId) {
        return AGENT_INFORMATION.get(policyId);
    }
    
    /**
     * Get all available detailed policy IDs
     */
    public static String[] getAllDetailedPolicyIds() {
        return new String[]{"POL12345", "POL67890", "POL11111", "POL99999"};
    }
    
    /**
     * Get policies by status
     */
    public static List<DetailedPolicyInfo> getPoliciesByStatus(String status) {
        List<DetailedPolicyInfo> result = new ArrayList<>();
        DetailedPolicyInfo[] allPolicies = {
            ACTIVE_AUTO_POLICY_DETAILED,
            EXPIRED_HOME_POLICY_DETAILED,
            PENDING_LIFE_POLICY_DETAILED,
            ACTIVE_HEALTH_POLICY_DETAILED
        };
        
        for (DetailedPolicyInfo policy : allPolicies) {
            if (policy.status.equalsIgnoreCase(status)) {
                result.add(policy);
            }
        }
        
        return result;
    }
    
    /**
     * Get policies by type
     */
    public static List<DetailedPolicyInfo> getPoliciesByType(String type) {
        List<DetailedPolicyInfo> result = new ArrayList<>();
        DetailedPolicyInfo[] allPolicies = {
            ACTIVE_AUTO_POLICY_DETAILED,
            EXPIRED_HOME_POLICY_DETAILED,
            PENDING_LIFE_POLICY_DETAILED,
            ACTIVE_HEALTH_POLICY_DETAILED
        };
        
        for (DetailedPolicyInfo policy : allPolicies) {
            if (policy.type.equalsIgnoreCase(type)) {
                result.add(policy);
            }
        }
        
        return result;
    }
}
