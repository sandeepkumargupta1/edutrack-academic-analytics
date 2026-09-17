"""
EduTrack Project Report PDF Generator.
Compiles docs/PROJECT_REPORT.md into a beautifully formatted, print-ready PDF document
using ReportLab, ready for direct upload to the VITyarthi submission portal.
"""

import sys
from pathlib import Path
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate,
    Paragraph,
    Spacer,
    Preformatted,
    KeepTogether,
    HRFlowable,
)

BASE_DIR = Path(__file__).resolve().parent
DOCS_DIR = BASE_DIR / "docs"
REPORT_MD = DOCS_DIR / "PROJECT_REPORT.md"
REPORT_PDF = DOCS_DIR / "PROJECT_REPORT.pdf"


def build_pdf():
    print(f"Reading markdown report from: {REPORT_MD}")
    if not REPORT_MD.exists():
        print(f"Error: {REPORT_MD} does not exist.")
        sys.exit(1)

    with open(REPORT_MD, "r", encoding="utf-8") as f:
        lines = f.readlines()

    doc = SimpleDocTemplate(
        str(REPORT_PDF),
        pagesize=letter,
        rightMargin=40,
        leftMargin=40,
        topMargin=40,
        bottomMargin=40,
    )

    styles = getSampleStyleSheet()

    # Custom styles
    title_style = ParagraphStyle(
        "ReportTitle",
        parent=styles["Heading1"],
        fontName="Helvetica-Bold",
        fontSize=20,
        leading=24,
        textColor=colors.HexColor("#1e3a8a"),
        spaceAfter=15,
    )

    h1_style = ParagraphStyle(
        "ReportH1",
        parent=styles["Heading2"],
        fontName="Helvetica-Bold",
        fontSize=13,
        leading=17,
        textColor=colors.HexColor("#1e40af"),
        spaceBefore=14,
        spaceAfter=6,
        keepWithNext=True,
    )

    h2_style = ParagraphStyle(
        "ReportH2",
        parent=styles["Heading3"],
        fontName="Helvetica-Bold",
        fontSize=10.5,
        leading=14,
        textColor=colors.HexColor("#0f766e"),
        spaceBefore=8,
        spaceAfter=4,
        keepWithNext=True,
    )

    body_style = ParagraphStyle(
        "ReportBody",
        parent=styles["Normal"],
        fontName="Helvetica",
        fontSize=9,
        leading=12.5,
        textColor=colors.HexColor("#1f2937"),
        spaceAfter=6,
    )

    code_style = ParagraphStyle(
        "ReportCode",
        fontName="Courier",
        fontSize=6.8,
        leading=8.2,
        textColor=colors.HexColor("#111827"),
    )

    story = []

    in_code_block = False
    code_lines = []

    for raw_line in lines:
        line = raw_line.rstrip("\r\n")

        # Code block handling
        if line.startswith("```"):
            if in_code_block:
                code_text = "\n".join(code_lines)
                story.append(
                    KeepTogether(
                        [
                            Preformatted(code_text, code_style),
                            Spacer(1, 4),
                        ]
                    )
                )
                code_lines = []
                in_code_block = False
            else:
                in_code_block = True
            continue

        if in_code_block:
            code_lines.append(line)
            continue

        # Markdown headings & content
        stripped = line.strip()
        if not stripped:
            story.append(Spacer(1, 3))
            continue

        if stripped.startswith("# "):
            story.append(Paragraph(stripped[2:], title_style))
            story.append(HRFlowable(width="100%", thickness=1.5, color=colors.HexColor("#1e40af"), spaceAfter=10))
        elif stripped.startswith("## "):
            story.append(Spacer(1, 6))
            story.append(Paragraph(stripped[3:], h1_style))
            story.append(HRFlowable(width="100%", thickness=0.7, color=colors.HexColor("#cbd5e1"), spaceAfter=6))
        elif stripped.startswith("### "):
            story.append(Paragraph(stripped[4:], h2_style))
        elif stripped.startswith("- ") or stripped.startswith("* "):
            story.append(Paragraph(f"&bull; {stripped[2:]}", body_style))
        elif stripped.startswith("---"):
            story.append(Spacer(1, 4))
        else:
            # Regular paragraph
            story.append(Paragraph(stripped, body_style))

    doc.build(story)
    print(f"[SUCCESS] Official PDF project report compiled: {REPORT_PDF}")


if __name__ == "__main__":
    build_pdf()
