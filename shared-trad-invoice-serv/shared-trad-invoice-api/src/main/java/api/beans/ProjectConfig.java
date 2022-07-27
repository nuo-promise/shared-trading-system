package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConfig {

    private String id;

    private String projectNo;

    private Boolean invoice;

    private InvoiceConfig invoiceConfig;
}
